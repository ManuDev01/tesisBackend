package com.tesis.urbe.examen.service;

import com.tesis.urbe.examen.dto.ExamenDTO;
import com.tesis.urbe.examen.entity.ExamenEntity;
import com.tesis.urbe.examen.repository.ExamenRepository;
import org.springframework.stereotype.Service;

@Service
public class ExamenService {

    private final ExamenRepository examenRepository;

    public ExamenService(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    public ExamenDTO getExamenByIdSeccion(Integer idSeccion) {
        ExamenEntity examen = examenRepository.findBySeccion_IdSeccion(idSeccion)
                .orElseThrow(() -> new RuntimeException("No se encontró examen para la sección ID: " + idSeccion));

        return new ExamenDTO(
                examen.getIdExamen(),
                examen.getSeccion().getIdSeccion(),
                examen.getTitulo(),
                examen.getDescripcion(),
                examen.getPuntosAumento(),
                false // Mapear con tabla de resultados/completados si aplica
        );
    }
}