package com.proyecto.reservas.service;

import com.proyecto.reservas.exception.RecursoNoEncontradoException;
import com.proyecto.reservas.model.Espacio;
import com.proyecto.reservas.repository.EspacioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EspacioService {
    private final EspacioRepository repository;

    public EspacioService(EspacioRepository repository) {
        this.repository = repository;
    }

    public Espacio crear(Espacio espacio) {
        validar(espacio);
        return repository.save(espacio);
    }

    public List<Espacio> listar() {
        return repository.findAll();
    }

    public Espacio buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Espacio no encontrado: " + id));
    }

    public Espacio actualizar(Long id, Espacio datos) {
        Espacio actual = buscar(id);
        validar(datos);
        actual.setNombre(datos.getNombre());
        actual.setTipo(datos.getTipo());
        actual.setCapacidad(datos.getCapacidad());
        actual.setCantidadEquipos(datos.getCantidadEquipos());
        actual.setSonidoProfesional(datos.isSonidoProfesional());
        actual.setEnMantenimiento(datos.isEnMantenimiento());
        return repository.save(actual);
    }

    public Espacio cambiarMantenimiento(Long id, boolean activo) {
        Espacio espacio = buscar(id);
        espacio.setEnMantenimiento(activo);
        return repository.save(espacio);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Espacio no encontrado: " + id);
        }
        repository.deleteById(id);
    }

    private void validar(Espacio e) {
        if (e.getNombre() == null || e.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del espacio es obligatorio.");
        }
        if (e.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de espacio es obligatorio.");
        }
        if (e.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        if (e.getTipo().name().equals("LABORATORIO")
                && (e.getCantidadEquipos() == null || e.getCantidadEquipos() <= 0)) {
            throw new IllegalArgumentException("El laboratorio debe tener equipos registrados.");
        }
    }
}
