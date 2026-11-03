package com.tesis.urbe.proyectos.service;

import com.tesis.urbe.proyectos.entity.EvaluacionProyectoCursoEntity;
import com.tesis.urbe.proyectos.repository.EvaluacionProyectoCursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionProyectoCursoService {

    private final EvaluacionProyectoCursoRepository evaluacionCursoRepository;

    public EvaluacionProyectoCursoService(EvaluacionProyectoCursoRepository evaluacionCursoRepository) {
        this.evaluacionCursoRepository = evaluacionCursoRepository;
    }

    public EvaluacionProyectoCursoEntity guardarEvaluacion(Integer idUsuario, Integer idProyectoCurso, String codigo, String resultado, String modelo, String versionPrompt) {
        EvaluacionProyectoCursoEntity evaluacion = new EvaluacionProyectoCursoEntity();
        evaluacion.setIdUsuario(idUsuario);
        evaluacion.setIdProyectoCurso(idProyectoCurso);
        evaluacion.setCodigo(codigo);
        evaluacion.setResultados(resultado);
        evaluacion.setModelo(modelo);
        evaluacion.setVersionPrompt(versionPrompt);
        return evaluacionCursoRepository.save(evaluacion);
    }

    public List<EvaluacionProyectoCursoEntity> obtenerHistorial(Integer idUsuario, Integer idProyectoCurso) {
        return evaluacionCursoRepository.findByIdUsuarioAndIdProyectoCurso(idUsuario, idProyectoCurso);
    }

    public Optional<EvaluacionProyectoCursoEntity> obtenerUltimaEvaluacion(Integer idUsuario, Integer idProyectoCurso) {
        return evaluacionCursoRepository.findTopByIdUsuarioAndIdProyectoCursoOrderByIdEvaluacionProyectoCursoDesc(idUsuario, idProyectoCurso);
    }
}
