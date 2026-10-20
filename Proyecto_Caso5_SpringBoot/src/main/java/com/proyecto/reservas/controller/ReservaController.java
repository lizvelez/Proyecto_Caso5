package com.proyecto.reservas.controller;

import com.proyecto.reservas.model.Reserva;
import com.proyecto.reservas.service.ReservaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @PostMapping
    public Reserva crear(@RequestBody ReservaService.CrearReservaRequest datos) {
        return service.crear(datos);
    }

    @GetMapping
    public List<Reserva> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Reserva buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PatchMapping("/{id}/aprobar")
    public Reserva aprobar(@PathVariable Long id) {
        return service.aprobar(id);
    }

    @PatchMapping("/{id}/rechazar")
    public Reserva rechazar(@PathVariable Long id, @RequestParam String motivo) {
        return service.rechazar(id, motivo);
    }

    @PatchMapping("/{id}/cancelar")
    public Reserva cancelar(@PathVariable Long id, @RequestParam String motivo) {
        return service.cancelar(id, motivo);
    }
}
