package app;

import app.models.*;
import app.repository.*;
import app.repository.OrdenMedicaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class Proyecto implements CommandLineRunner {
    
    private final MascotaRepository mascotaRepository;
    private final UsuariosRepository usuariosRepository;
    private final RegistroClinicoRepository registroClinicoRepository;
    private OrdenMedicaRepository ordenRepository;
    
    @Autowired
    public Proyecto(MascotaRepository mascotaRepository, 
                UsuariosRepository usuariosRepository, 
                RegistroClinicoRepository registroClinicoRepository, 
                OrdenMedicaRepository ordenMedicaRepository) {
    this.mascotaRepository = mascotaRepository;
    this.usuariosRepository = usuariosRepository;
    this.registroClinicoRepository = registroClinicoRepository;
    this.ordenRepository = ordenMedicaRepository;
}


    
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\nSeleccione una opcion:");
            System.out.println("1. Registrar persona");
            System.out.println("2. Iniciar sesion");
            System.out.println("3. Salir");
            System.out.print("Opcion: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> registrarPersona(scanner);
                case 2 -> iniciarSesion(scanner);
                case 3 -> continuar = false;
                default -> System.out.println("Opcion no valida.");
            }
        }
        scanner.close();
    }

    private void registrarPersona(Scanner scanner) {
    System.out.print("Ingrese la cedula para registrar la nueva persona: ");
    String cedula = scanner.nextLine();

    if (usuariosRepository.findByCedula(cedula).isPresent()) {
        System.out.println("Ya existe una persona registrada con esa cedula.");
        return;
    }

    System.out.print("Ingrese nombre: ");
    String nombre = scanner.nextLine();
    System.out.print("Ingrese edad: ");
    int edad = scanner.nextInt();
    scanner.nextLine();

    System.out.println("Seleccione rol (ingrese solo el numero):");
    System.out.println("1. ADMINISTRADOR");
    System.out.println("2. VENDEDOR");
    System.out.println("3. VETERINARIO");
    System.out.println("4. DUEÑO");

    int rolOpcion = scanner.nextInt();
    scanner.nextLine();

    Rol rol;
    switch (rolOpcion) {
        case 1:
            rol = Rol.ADMINISTRADOR;
            break;
        case 2:
            rol = Rol.VENDEDOR;
            break;
        case 3:
            rol = Rol.VETERINARIO;
            break;
        case 4:
            rol = Rol.DUEÑO;
            break;
        default:
            System.out.println("Opcion no valida.");
            return;
    }

    String username = "";
    String password = "";

    if (rol == Rol.VENDEDOR || rol == Rol.VETERINARIO) {
        System.out.println("Para registrar este rol, debe autenticarse como ADMINISTRADOR.");
        System.out.print("Ingrese usuario administrador: ");
        String usuarioAdmin = scanner.nextLine();
        System.out.print("Ingrese contraseña administrador: ");
        String contrasenaAdmin = scanner.nextLine();

        if (!(usuarioAdmin.equals("admin") && contrasenaAdmin.equals("admin123"))) {
            System.out.println("Usuario o contraseña incorrectos. No tiene permisos para registrar vendedores o veterinarios.");
            return;
        }
    }

    if (rol == Rol.ADMINISTRADOR) {
        System.out.println("Registrando nuevo Administrador...");
    }

    if (rol != Rol.DUEÑO) {
        System.out.print("Ingrese username: ");
        username = scanner.nextLine();
        System.out.print("Ingrese contraseña: ");
        password = scanner.nextLine();
    }

    Usuarios nuevoUsuario = new Usuarios(cedula, nombre, edad, rol, username, password);
    usuariosRepository.save(nuevoUsuario);
    System.out.println("Usuario registrado exitosamente.");
}

    private void iniciarSesion(Scanner scanner) {
        System.out.print("Ingrese username: ");
        String username = scanner.nextLine();
        System.out.print("Ingrese contraseña: ");
        String password = scanner.nextLine();

        Optional<Usuarios> usuarioOpt = usuariosRepository.findByUsername(username);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getPassword().equals(password)) {
            System.out.println("Usuario o contraseña incorrectos.");
            return;
        }

        Usuarios usuario = usuarioOpt.get();
        switch (usuario.getRol()) {
            case ADMINISTRADOR -> menuAdministrador(scanner);
            case VETERINARIO -> menuVeterinario(scanner, usuario);
            case VENDEDOR -> System.out.println("Bienvenido, vendedor. (Menu aun no implementado)");
            default -> System.out.println("Solo administradores, veterinarios y vendedores pueden iniciar sesion.");
        }
    }

    private void menuAdministrador(Scanner scanner) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\nMenu de Administrador:");
            System.out.println("1. Registrar veterinario/vendedor");
            System.out.println("2. Listar personas registradas");
            System.out.println("3. Cerrar sesion");
            System.out.print("Opcion: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> registrarPersona(scanner);
                case 2 -> listarPersonas();
                case 3 -> continuar = false;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private void menuVeterinario(Scanner scanner, Usuarios veterinario) {
    boolean continuar = true;
    while (continuar) {
        System.out.println("\nMenu de Veterinario:");
        System.out.println("1. Ver historial clinico de mascota");
        System.out.println("2. Agregar nuevo registro clinico");
        System.out.println("3. Registrar nueva mascota");
        System.out.println("4. Cerrar sesion");
        System.out.print("Opcion: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1 -> verHistorialClinico(scanner);
            case 2 -> registrarRegistroClinico(scanner, veterinario);
            case 3 -> registrarMascota(scanner);
            case 4 -> continuar = false;
            default -> System.out.println("Opcion no válida.");
        }
    }
}

private void registrarMascota(Scanner scanner) {
    System.out.println("Registrar nueva mascota:");

    System.out.print("Ingrese nombre de la mascota: ");
    String nombre = scanner.nextLine();

    System.out.print("Ingrese cedula del dueño: ");
    String cedulaDueno = scanner.nextLine();

    Optional<Usuarios> usuarioOpt = usuariosRepository.findByCedula(cedulaDueno);
    
    if (usuarioOpt.isEmpty() || usuarioOpt.get().getRol() != Rol.DUEÑO) {
        System.out.println("No se encontro un dueño con esa cédula o el usuario no es dueño.");
        return;
    }

    Usuarios dueno = usuarioOpt.get();

    System.out.print("Ingrese la especie (perro, gato, etc.): ");
    String especie = scanner.nextLine();

    System.out.print("Ingrese la raza de la mascota: ");
    String raza = scanner.nextLine();

    System.out.print("Ingrese las caracteristicas (color, tamaño): ");
    String caracteristicas = scanner.nextLine();

    System.out.print("Ingrese peso (en kg): ");
    double peso = scanner.nextDouble();
    scanner.nextLine();

    System.out.print("Ingrese edad de la mascota: ");
    int edad = scanner.nextInt();
    scanner.nextLine();

    Mascota nuevaMascota = new Mascota(nombre, dueno, edad, especie, raza, caracteristicas, peso);

    mascotaRepository.save(nuevaMascota);
    System.out.println("Mascota registrada exitosamente.");
}

private void registrarRegistroClinico(Scanner scanner, Usuarios veterinario) {
    System.out.print("Ingrese el ID de la mascota: ");
    Long idMascota = scanner.nextLong();
    scanner.nextLine();

    Optional<Mascota> mascotaOpt = mascotaRepository.findById(idMascota);
    if (mascotaOpt.isEmpty()) {
        System.out.println("No se encontró una mascota con ese ID.");
        return;
    }

    Mascota mascota = mascotaOpt.get();
    LocalDate fecha = LocalDate.now();
    
    System.out.print("Motivo de consulta: ");
    String motivoConsulta = scanner.nextLine();

    System.out.print("Sintomatologia: ");
    String sintomatologia = scanner.nextLine();

    System.out.print("Diagnostico: ");
    String diagnostico = scanner.nextLine();

    System.out.print("Procedimiento (dejar vacio si no aplica): ");
    String procedimiento = scanner.nextLine();

    System.out.print("Medicamento (dejar vacio si no aplica): ");
    String medicamento = scanner.nextLine();

    System.out.print("Dosis (dejar vacio si no aplica): ");
    String dosis = scanner.nextLine();

    String idOrden = (medicamento.isEmpty()) ? "" : "ORD" + System.currentTimeMillis();

    if (!medicamento.isEmpty()) {
    OrdenMedica nuevaOrden = new OrdenMedica(mascota,mascota.getDueno(),veterinario,medicamento,dosis);
        ordenRepository.save(nuevaOrden);
        System.out.println("Orden generada y guardada exitosamente.");
    }
    
    String vacuna = null;
    if ("vacunacion".equalsIgnoreCase(procedimiento.trim())) {
        if (!(mascota.getEspecie().trim().equalsIgnoreCase("perro") || 
              mascota.getEspecie().trim().equalsIgnoreCase("gato"))) {
            System.out.println("Error! El procedimiento de vacunacion solo es valido para perros y gatos.");
            return;
        }

        System.out.print("Nombre de la vacuna: ");
        vacuna = scanner.nextLine();

        if (vacuna.isEmpty()) {
            System.out.println("¡Error! Debe proporcionar el nombre de la vacuna.");
            return;
        }
    }

    String historialVacunacion = null;
    if ("vacunacion".equalsIgnoreCase(procedimiento.trim()) && vacuna != null) {
        historialVacunacion = "Vacuna: " + vacuna;
    }

    System.out.print("Medicamentos a los que presenta alergia (dejar vacio si no aplica): ");
    String medicamentosAlergia = scanner.nextLine();

    System.out.print("Detalle del procedimiento: ");
    String detalleProcedimiento = scanner.nextLine();

    RegistroClinico nuevoRegistro = new RegistroClinico(
    fecha, mascota, veterinario, motivoConsulta, sintomatologia, diagnostico, procedimiento, medicamento,
    dosis, idOrden, historialVacunacion, medicamentosAlergia, detalleProcedimiento, false
);

    registroClinicoRepository.save(nuevoRegistro);
    System.out.println("Registro clinico guardado exitosamente.");
}

    private void verHistorialClinico(Scanner scanner) {
    System.out.print("Ingrese la cedula del dueño de la mascota: ");
    String cedula = scanner.nextLine();
    
    List<Mascota> mascotas = mascotaRepository.findByDuenoCedula(cedula);
    if (mascotas.isEmpty()) {
        System.out.println("No se encontraron mascotas para este dueño.");
        return;
    }

    for (int i = 0; i < mascotas.size(); i++) {
        System.out.println((i + 1) + ". " + mascotas.get(i).getNombre());
    }

    System.out.print("Seleccione una mascota para ver su historial clinico (numero): ");
    int mascotaSeleccionada = scanner.nextInt();
    scanner.nextLine(); 

    if (mascotaSeleccionada < 1 || mascotaSeleccionada > mascotas.size()) {
        System.out.println("Opción no valida.");
        return;
    }
    
    Mascota mascota = mascotas.get(mascotaSeleccionada - 1);

    List<RegistroClinico> historial = registroClinicoRepository.findByMascota(mascota);

    if (historial.isEmpty()) {
        System.out.println("No hay historial clinico para esta mascota.");
        return;
    }

    for (int i = 0; i < historial.size(); i++) {
        RegistroClinico registro = historial.get(i);
        System.out.println((i + 1) + ". Fecha: " + registro.getFecha() + " | Diagnostico: " + registro.getDiagnostico());
    }

    System.out.print("Seleccione un registro para editar (numero): ");
    int registroSeleccionado = scanner.nextInt();
    scanner.nextLine(); 

    if (registroSeleccionado < 1 || registroSeleccionado > historial.size()) {
        System.out.println("Opción no valida.");
        return;
    }

    RegistroClinico registroSeleccionadoObj = historial.get(registroSeleccionado - 1);

    System.out.println("\nRegistro seleccionado:");
    System.out.println("Motivo de consulta: " + registroSeleccionadoObj.getMotivoConsulta());
    System.out.println("Sintomatologia: " + registroSeleccionadoObj.getSintomatologia());
    System.out.println("Diagnostico: " + registroSeleccionadoObj.getDiagnostico());
    System.out.println("Procedimiento: " + registroSeleccionadoObj.getProcedimiento());
    System.out.println("Medicamento: " + registroSeleccionadoObj.getMedicamento());
    System.out.println("Dosis: " + registroSeleccionadoObj.getDosis());
    System.out.println("Alergia a medicamentos: " + registroSeleccionadoObj.getMedicamentosAlergia());
    System.out.println("Detalle del procedimiento: " + registroSeleccionadoObj.getDetalleProcedimiento());

    System.out.println("\n¿Que desea editar?");
    System.out.println("1. Motivo de consulta");
    System.out.println("2. Sintomatologia");
    System.out.println("3. Diagnostico");
    System.out.println("4. Procedimiento");
    System.out.println("5. Medicamento");
    System.out.println("6. Dosis");
    System.out.println("7. Alergia a medicamentos");
    System.out.println("8. Detalle del procedimiento");
    System.out.println("9. No editar");
    System.out.print("Seleccione una opcion: ");
    int opcionEdicion = scanner.nextInt();
    scanner.nextLine();

    switch (opcionEdicion) {
        case 1:
            System.out.print("Ingrese nuevo motivo de consulta: ");
            registroSeleccionadoObj.setMotivoConsulta(scanner.nextLine());
            break;
        case 2:
            System.out.print("Ingrese nueva sintomatologia: ");
            registroSeleccionadoObj.setSintomatologia(scanner.nextLine());
            break;
        case 3:
            System.out.print("Ingrese nuevo diagnóstico: ");
            registroSeleccionadoObj.setDiagnostico(scanner.nextLine());
            break;
        case 4:
            System.out.print("Ingrese nuevo procedimiento: ");
            registroSeleccionadoObj.setProcedimiento(scanner.nextLine());
            break;
        case 5:
            System.out.print("Ingrese nuevo medicamento: ");
            registroSeleccionadoObj.setMedicamento(scanner.nextLine());
            break;
        case 6:
            System.out.print("Ingrese nueva dosis: ");
            registroSeleccionadoObj.setDosis(scanner.nextLine());
            break;
        case 7:
            System.out.print("Ingrese nuevos medicamentos a los que presenta alergia: ");
            registroSeleccionadoObj.setMedicamentosAlergia(scanner.nextLine());
            break;
        case 8:
            System.out.print("Ingrese nuevo detalle del procedimiento: ");
            registroSeleccionadoObj.setDetalleProcedimiento(scanner.nextLine());
            break;
        case 9:
            System.out.println("No se realizaron cambios.");
            return;
        default:
            System.out.println("Opción no valida.");
            return;
    }

    registroClinicoRepository.save(registroSeleccionadoObj);
    System.out.println("Registro clinico actualizado exitosamente.");
}

    private void listarPersonas() {
        List<Usuarios> usuarios = usuariosRepository.findAll();
        if (usuarios.isEmpty()) {
            System.out.println("No hay personas registradas.");
        } else {
            usuarios.forEach(usuario -> System.out.println(usuario.getNombre() + " - " + usuario.getRol()));
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Proyecto.class, args);
    }
}
