package com.tesis.urbe.certificados.repository;

import com.tesis.urbe.certificados.entity.CertificadosEntity;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;

public interface CertificadosRepository extends ListCrudRepository<CertificadosEntity, Integer> {
    List<CertificadosEntity> findByIdUsuario(Integer idUsuario);
}