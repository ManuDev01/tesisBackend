package com.tesis.urbe.quizz.repository;

import com.tesis.urbe.quizz.entity.QuizzEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QuizzRepository extends ListCrudRepository<QuizzEntity, Integer> {

    List<QuizzEntity> findByIdLeccion_IdLeccion(Integer idLeccion);

    // Consulta en lote usada por GET /quizz/getQuizzByLecciones (antes sin usar).
    @Query("SELECT q FROM QuizzEntity q WHERE q.idLeccion.idLeccion IN :idsLecciones ORDER BY q.idQuizz")
    List<QuizzEntity> findByIdLeccionInLeccionId(@Param("idsLecciones") List<Integer> idsLecciones);
}
