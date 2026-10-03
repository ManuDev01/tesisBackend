package com.tesis.urbe.examen.repository;

import com.tesis.urbe.examen.entity.UsuarioEnExamenEntity; // Ajusta el paquete según corresponda
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioEnExamenRepository extends ListCrudRepository<UsuarioEnExamenEntity, Integer> {
    boolean existsByIdUsuarioAndIdExamen(Integer idUsuario, Integer idExamen);
}