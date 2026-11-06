package com.tesis.urbe.leccion.repository;

import com.tesis.urbe.leccion.entity.LeccionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LeccionRepository extends ListCrudRepository<LeccionEntity, Integer> {

    @Query("SELECT l FROM LeccionEntity l LEFT JOIN FETCH l.idSeccion WHERE l.idSeccion.idSeccion = :idSeccion")
    List<LeccionEntity> findByIdSeccion_IdSeccion(@Param("idSeccion") Integer idSeccion);

    @Query("SELECT DISTINCT l FROM LeccionEntity l WHERE l.idLeccion IN :idsLecciones")
    List<LeccionEntity> findByIdLeccionIn(@Param("idsLecciones") List<Integer> idsLecciones);

    // Con la sección ya cargada: LeccionDTO.fromEntity toca getIdSeccion() y, sin
    // este fetch, disparaba 1 consulta perezosa por sección (N+1) al serializar.
    @Query("SELECT l FROM LeccionEntity l LEFT JOIN FETCH l.idSeccion")
    List<LeccionEntity> findAllConSeccion();
}
