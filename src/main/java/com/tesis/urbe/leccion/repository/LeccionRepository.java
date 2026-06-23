package com.tesis.urbe.leccion.repository;

import com.tesis.urbe.leccion.entity.LeccionEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface LeccionRepository extends ListCrudRepository<LeccionEntity, Integer> {
}
