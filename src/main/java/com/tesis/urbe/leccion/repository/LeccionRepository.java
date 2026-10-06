package com.tesis.urbe.leccion.repository;

import com.tesis.urbe.leccion.entity.LeccionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeccionRepository extends ListCrudRepository<LeccionEntity, Integer> {

    List<LeccionEntity> findByIdSeccion_IdSeccion(Integer idSeccion);
    List<LeccionEntity> findByIdLeccionIn(List<Integer> idsLecciones);

    @Query("SELECT l FROM LeccionEntity l WHERE l.idSeccion.idSeccion = :idSeccion")
    List<LeccionEntity> findBySeccionId(@Param("idSeccion") Integer idSeccion);
}
