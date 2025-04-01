package app.models;

import app.models.Usuarios;
import java.util.Optional;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("USUARIO")
public class Usuarios extends Persona {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public Usuarios() {
        super();
    }

    public Usuarios(String cedula, String nombre, int edad, Rol rol, String username, String password) {
        super(cedula, nombre, edad, rol);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
