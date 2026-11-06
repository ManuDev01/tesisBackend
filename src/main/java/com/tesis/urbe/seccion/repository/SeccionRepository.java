package com.tesis.urbe.seccion.repository;

import com.tesis.urbe.seccion.entity.SeccionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SeccionRepository extends ListCrudRepository<SeccionEntity, Integer> {

    // SeccionEntity es plana (no tiene colecciones lecciones/examen/proyectoCurso),
    // por lo que no se puede aplicar un @EntityGraph sobre esos atributos.
    @Query("SELECT s FROM SeccionEntity s WHERE s.idCurso = :idCurso ORDER BY s.orden ASC")
    List<SeccionEntity> findByIdCursoOrderByOrdenAsc(@Param("idCurso") Integer idCurso);
}
