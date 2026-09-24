package com.tesis.urbe.medallas.repository;

import com.tesis.urbe.medallas.entity.UsuarioConMedallaEntity;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;

public interface UsuarioConMedallaRepository extends ListCrudRepository<UsuarioConMedallaEntity, Integer> {
    List<UsuarioConMedallaEntity> findByIdUsuario(Integer idUsuario);
}