package com.tesis.urbe.proyectos.service;

import com.tesis.urbe.proyectos.entity.EvaluacionProyectoEntity;
import com.tesis.urbe.proyectos.repository.EvaluacionProyectoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionProyectoService {

    private final EvaluacionProyectoRepository evaluacionRepository;

    public EvaluacionProyectoService(EvaluacionProyectoRepository evaluacionRepository) {
        this.evaluacionRepository = evaluacionRepository;
    }

    public EvaluacionProyectoEntity guardarEvaluacion(Integer idUsuario, Integer idProyecto, String codigo, String resultado, String modelo, String versionPrompt) {
        EvaluacionProyectoEntity evaluacion = new EvaluacionProyectoEntity();
        evaluacion.setIdUsuario(idUsuario);
        evaluacion.setIdProyecto(idProyecto);
        evaluacion.setCodigo(codigo);
        evaluacion.setResultado(resultado);
        evaluacion.setModelo(modelo);
        evaluacion.setVersionPrompt(versionPrompt);
        return evaluacionRepository.save(evaluacion);
    }

    public List<EvaluacionProyectoEntity> obtenerHistorial(Integer idUsuario, Integer idProyecto) {
        return evaluacionRepository.findByIdUsuarioAndIdProyecto(idUsuario, idProyecto);
    }

    public Optional<EvaluacionProyectoEntity> obtenerUltimaEvaluacion(Integer idUsuario, Integer idProyecto) {
        return evaluacionRepository.findTopByIdUsuarioAndIdProyectoOrderByIdEvaluacionProyectoDesc(idUsuario, idProyecto);
    }
}
