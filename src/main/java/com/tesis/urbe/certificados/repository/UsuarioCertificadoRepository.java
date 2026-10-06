package com.tesis.urbe.certificados.repository;

import com.tesis.urbe.certificados.entity.UsuarioCertificadoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioCertificadoRepository extends ListCrudRepository<UsuarioCertificadoEntity, Integer> {
    List<UsuarioCertificadoEntity> findByIdUsuario(Integer idUsuario);
    Optional<UsuarioCertificadoEntity> findByUidCertificado(String uidCertificado);
    boolean existsByIdUsuarioAndIdCertificado(Integer idUsuario, Integer idCertificado);

    @Query("""
        SELECT c.idCertificado, c.titulo, uc.idUsuario, u.primerNombre, u.primerApellido
        FROM UsuarioCertificadoEntity uc
        JOIN CertificadosEntity c ON uc.idCertificado = c.idCertificado
        JOIN UserEntity u ON uc.idUsuario = u.idUsuario
    """)
    List<Object[]> findConstanciasConDetalleUsuario();
}