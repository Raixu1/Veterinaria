package app.controller;

import app.models.Persona;
import app.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @GetMapping
    public List<Persona> listarPersonas() {
        return personaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Optional<Persona> obtenerPersona(@PathVariable Long id) {
        return personaService.obtenerPorId(id);
    }

    @PostMapping
    public Persona crearPersona(@RequestBody Persona persona) {
        return personaService.guardar(persona);
    }

    @DeleteMapping("/{id}")
    public String eliminarPersona(@PathVariable Long id) {
        personaService.eliminar(id);
        return "Persona eliminada con éxito";
    }
}