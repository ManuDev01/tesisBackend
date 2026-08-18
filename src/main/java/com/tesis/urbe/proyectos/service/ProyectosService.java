package com.tesis.urbe.proyectos.service;


import com.tesis.urbe.proyectos.dto.ProyectosDTO;
import com.tesis.urbe.proyectos.repository.ProyectosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProyectosService {

    private final ProyectosRepository proyectosRepository;

    public ProyectosService(ProyectosRepository proyectosRepository) {
        this.proyectosRepository = proyectosRepository;
    }

    public List<ProyectosDTO> getProyectos() {
        return proyectosRepository.findAll()
                .stream()
                .map(ProyectosDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
