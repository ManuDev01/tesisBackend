package com.tesis.urbe.seccion.repository;

import com.tesis.urbe.seccion.entity.SeccionEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SeccionRepository extends ListCrudRepository<SeccionEntity, Integer> {
    List<SeccionEntity> findByIdCursoOrderByOrdenAsc(Integer idCurso);
}
