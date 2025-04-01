package app.controller;

import app.models.RegistroClinico;
import app.service.RegistroClinicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-clinicos")
public class RegistroClinicoController {

    @Autowired
    private RegistroClinicoService registroClinicoService;

    @PostMapping("/guardar")
    public RegistroClinico guardarRegistro(@RequestBody RegistroClinico registro) {
        return registroClinicoService.guardarRegistro(registro);
    }

    @GetMapping("/listar")
    public List<RegistroClinico> obtenerTodosLosRegistros() {
        return registroClinicoService.obtenerTodosLosRegistros();
    }
    
    @GetMapping("/{id}")
    public RegistroClinico obtenerRegistroPorId(@PathVariable Long id) {
        return registroClinicoService.obtenerRegistroPorId(id); 
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarRegistro(@PathVariable Long id) {
        registroClinicoService.eliminarRegistro(id);
    }
}


