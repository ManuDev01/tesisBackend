package com.tesis.urbe.quizz.repository;

import com.tesis.urbe.quizz.dto.QuizzFalloDashboardDTO;
import com.tesis.urbe.quizz.entity.FalloUsuarioQuizzEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface FalloUsuarioQuizzRepository extends ListCrudRepository<FalloUsuarioQuizzEntity, Integer> {

    List<FalloUsuarioQuizzEntity> findByUsuario_IdUsuario(Integer idUsuario);

    @Query("""
        SELECT new com.tesis.urbe.quizz.dto.QuizzFalloDashboardDTO(
            q.idQuizz,
            q.pregunta,
            s.titulo,
            COUNT(f)
        )
        FROM FalloUsuarioQuizzEntity f
        JOIN f.quizz q
        JOIN q.idLeccion l
        JOIN l.idSeccion s
        GROUP BY q.idQuizz, q.pregunta, s.titulo
        ORDER BY COUNT(f) DESC
    """)
    List<QuizzFalloDashboardDTO> findQuizzConMasFallos(Pageable pageable);
}
