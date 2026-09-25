package com.tesis.urbe.medallas.repository;

import com.tesis.urbe.medallas.entity.MedallaEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface MedallaRepository extends ListCrudRepository<MedallaEntity, Integer> {
    List<MedallaEntity> findByIdMedallaIn(List<Integer> idsMedallas);
}
