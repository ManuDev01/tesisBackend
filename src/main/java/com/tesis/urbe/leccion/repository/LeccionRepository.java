package com.tesis.urbe.leccion.repository;

import com.tesis.urbe.leccion.entity.LeccionEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface LeccionRepository extends ListCrudRepository<LeccionEntity, Integer> {

    List<LeccionEntity> findByIdSeccion_IdSeccion(Integer idSeccion);
    List<LeccionEntity> findByIdLeccionIn(List<Integer> idsLecciones);
}
