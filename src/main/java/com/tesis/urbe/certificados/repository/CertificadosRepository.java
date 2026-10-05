package com.tesis.urbe.certificados.repository;

import com.tesis.urbe.certificados.entity.CertificadosEntity;
import org.springframework.data.repository.ListCrudRepository;
import java.util.Optional;

public interface CertificadosRepository extends ListCrudRepository<CertificadosEntity, Integer> {
    Optional<CertificadosEntity> findByIdCurso(Integer idCurso);
}