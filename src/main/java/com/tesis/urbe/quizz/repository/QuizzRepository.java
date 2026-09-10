package com.tesis.urbe.quizz.repository;

import com.tesis.urbe.quizz.entity.QuizzEntity;
import com.tesis.urbe.seccion.entity.SeccionEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface QuizzRepository extends ListCrudRepository<QuizzEntity, Integer> {

    List<QuizzEntity> findByIdLeccion_IdLeccion(Integer idLeccion);
}
