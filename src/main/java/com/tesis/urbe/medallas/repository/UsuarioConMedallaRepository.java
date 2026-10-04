package com.tesis.urbe.medallas.repository;

import com.tesis.urbe.medallas.dto.UsuarioMedallaCountDTO;
import com.tesis.urbe.medallas.entity.UsuarioConMedallaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;

public interface UsuarioConMedallaRepository extends ListCrudRepository<UsuarioConMedallaEntity, Integer> {
    List<UsuarioConMedallaEntity> findByIdUsuario(Integer idUsuario);

    @Query("SELECT new com.tesis.urbe.medallas.dto.UsuarioMedallaCountDTO(" +
            "u.idUsuario, " +
            "CONCAT(u.primerNombre, ' ', u.primerApellido), " +
            "COUNT(um.idMedalla)) " +
            "FROM UsuarioConMedallaEntity um " +
            "JOIN com.tesis.urbe.user.entity.UserEntity u ON um.idUsuario = u.idUsuario " +
            "WHERE u.idRol.idRol != 2 " +
            "GROUP BY u.idUsuario, u.primerNombre, u.primerApellido " +
            "ORDER BY COUNT(um.idMedalla) DESC")
    List<UsuarioMedallaCountDTO> obtenerRankingUsuariosConMasMedallas();
}