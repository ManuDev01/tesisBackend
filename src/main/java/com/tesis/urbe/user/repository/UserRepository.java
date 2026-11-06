package com.tesis.urbe.user.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.tesis.urbe.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    // Busca coincidencia por nombreUsuario O por correo
    Optional<UserEntity> findByNombreUsuarioOrCorreo(String nombreUsuario, String correo);
    Optional<UserEntity> findByCorreo(String correo);

    /**
     * Experiencia total de un usuario (lecciones + proyectos + medallas) en UNA
     * sola consulta, en lugar de las 6 consultas sueltas que hacía UserService.obtainExp.
     *
     * @return la suma total o {@code vacío} si el usuario no existe.
     */
    @Query("""
            SELECT COALESCE((SELECT SUM(l.puntosLeccion) FROM LeccionEntity l WHERE l.idLeccion IN
                    (SELECT ue.idLeccion FROM UsuarioEnLeccionEntity ue WHERE ue.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(p.puntosProyectos) FROM ProyectosEntity p WHERE p.idProyecto IN
                    (SELECT up.idProyecto FROM UsuarioConProyectosEntity up WHERE up.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(m.valorMedalla) FROM MedallaEntity m WHERE m.idMedalla IN
                    (SELECT um.idMedalla FROM UsuarioConMedallaEntity um WHERE um.idUsuario = u.idUsuario)), 0L)
            FROM UserEntity u
            WHERE u.idUsuario = :idUsuario
            """)
    Optional<Long> calcularExpDeUsuario(@Param("idUsuario") Integer idUsuario);

    /**
     * Ranking de experiencia en UNA sola consulta: calcula la EXP con las mismas
     * tres subconsultas y une el rol en la propia consulta (sin cargas perezosas
     * por usuario). Excluye administradores, igual que el filtrado anterior.
     *
     * <p>Cada fila es: {@code [idUsuario, nombreUsuario, primerNombre, primerApellido, exp]}.
     */
    @Query("""
            SELECT u.idUsuario, u.nombreUsuario, u.primerNombre, u.primerApellido,
                 COALESCE((SELECT SUM(l.puntosLeccion) FROM LeccionEntity l WHERE l.idLeccion IN
                    (SELECT ue.idLeccion FROM UsuarioEnLeccionEntity ue WHERE ue.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(p.puntosProyectos) FROM ProyectosEntity p WHERE p.idProyecto IN
                    (SELECT up.idProyecto FROM UsuarioConProyectosEntity up WHERE up.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(m.valorMedalla) FROM MedallaEntity m WHERE m.idMedalla IN
                    (SELECT um.idMedalla FROM UsuarioConMedallaEntity um WHERE um.idUsuario = u.idUsuario)), 0L)
            FROM UserEntity u
            WHERE u.idRol IS NOT NULL AND u.idRol.idRol <> 2
            ORDER BY u.idUsuario
            """)
    List<Object[]> findRankingExpEstudiantes();
}