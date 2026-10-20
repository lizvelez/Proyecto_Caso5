package com.proyecto.reservas.controller;

import com.proyecto.reservas.model.Espacio;
import com.proyecto.reservas.service.EspacioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/espacios")
public class EspacioController {
    private final EspacioService service;

    public EspacioController(EspacioService service) {
        this.service = service;
    }

    @PostMapping
    public Espacio crear(@RequestBody Espacio espacio) {
        return service.crear(espacio);
    }

    @GetMapping
    public List<Espacio> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Espacio buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public Espacio actualizar(@PathVariable Long id, @RequestBody Espacio espacio) {
        return service.actualizar(id, espacio);
    }

    @PatchMapping("/{id}/mantenimiento")
    public Espacio mantenimiento(@PathVariable Long id, @RequestParam boolean activo) {
        return service.cambiarMantenimiento(id, activo);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
