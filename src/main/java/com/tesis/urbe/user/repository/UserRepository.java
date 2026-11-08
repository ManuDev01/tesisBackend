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

    // ── Fórmula única de EXP (compartida por las 3 queries de abajo) ──────────
    // Suma de las cinco tablas de progreso estilo "usuarioEn"/"usuarioCon":
    //   usuarioenleccion      -> leccion.puntosLeccion
    //   usuarioenexamen       -> examen.puntosaumento
    //   usuarioenproyectocurso-> proyectocurso.puntosproyecto
    //   usuarioconproyecto    -> proyectos.puntosproyectos
    //   usuarioconmedalla     -> medallas.valorMedalla
    // (JPQL no permite reutilizar fragmentos entre @Query, por eso se repite.)

    /**
     * Experiencia total de un usuario (las cinco tablas de progreso) en UNA
     * sola consulta, en lugar de las 6 consultas sueltas que hacía UserService.obtainExp.
     *
     * @return la suma total o {@code vacío} si el usuario no existe.
     */
    @Query("""
            SELECT COALESCE((SELECT SUM(l.puntosLeccion) FROM LeccionEntity l WHERE l.idLeccion IN
                    (SELECT ue.idLeccion FROM UsuarioEnLeccionEntity ue WHERE ue.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(e.puntosAumento) FROM ExamenEntity e WHERE e.idExamen IN
                    (SELECT ue.idExamen FROM UsuarioEnExamenEntity ue WHERE ue.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(pc.puntosProyecto) FROM ProyectoCursoEntity pc WHERE pc.idProyectoCurso IN
                    (SELECT up.idProyectoCurso FROM UsuarioEnProyectoCursoEntity up WHERE up.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(p.puntosProyectos) FROM ProyectosEntity p WHERE p.idProyecto IN
                    (SELECT up.idProyecto FROM UsuarioConProyectosEntity up WHERE up.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(m.valorMedalla) FROM MedallaEntity m WHERE m.idMedalla IN
                    (SELECT um.idMedalla FROM UsuarioConMedallaEntity um WHERE um.idUsuario = u.idUsuario)), 0L)
            FROM UserEntity u
            WHERE u.idUsuario = :idUsuario
            """)
    Optional<Long> calcularExpDeUsuario(@Param("idUsuario") Integer idUsuario);

    /**
     * EXP de CADA usuario en UNA sola consulta (cero N+1): la suma de las cinco
     * tablas de progreso se resuelve con subconsultas correlacionadas en el
     * SELECT, así el número de consultas es 1 sin importar cuántos usuarios
     * haya (antes: findAll + una consulta de EXP por usuario).
     *
     * <p>Nota sobre JOIN FETCH: no aplica aquí porque {@code UserEntity} no
     * tiene relaciones JPA hacia las tablas "usuarioEn" (guardan IDs sueltos) y
     * porque combinar fetch de colecciones con SUM provocaría un producto
     * cartesiano que inflaría los totales. La subconsulta correlacionada es el
     * equivalente sin N+1.
     *
     * <p>Cada fila es: {@code [idUsuario, nombreUsuario, exp]}.
     */
    @Query("""
            SELECT u.idUsuario, u.nombreUsuario,
                 COALESCE((SELECT SUM(l.puntosLeccion) FROM LeccionEntity l WHERE l.idLeccion IN
                    (SELECT ue.idLeccion FROM UsuarioEnLeccionEntity ue WHERE ue.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(e.puntosAumento) FROM ExamenEntity e WHERE e.idExamen IN
                    (SELECT ue.idExamen FROM UsuarioEnExamenEntity ue WHERE ue.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(pc.puntosProyecto) FROM ProyectoCursoEntity pc WHERE pc.idProyectoCurso IN
                    (SELECT up.idProyectoCurso FROM UsuarioEnProyectoCursoEntity up WHERE up.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(p.puntosProyectos) FROM ProyectosEntity p WHERE p.idProyecto IN
                    (SELECT up.idProyecto FROM UsuarioConProyectosEntity up WHERE up.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(m.valorMedalla) FROM MedallaEntity m WHERE m.idMedalla IN
                    (SELECT um.idMedalla FROM UsuarioConMedallaEntity um WHERE um.idUsuario = u.idUsuario)), 0L)
            FROM UserEntity u
            ORDER BY u.idUsuario
            """)
    List<Object[]> findExpDeTodosLosUsuarios();

    /**
     * EXP de UN usuario agrupada por curso/nivel (Básico, Intermedio,
     * Avanzado) en UNA sola consulta (cero N+1).
     *
     * <p>Solo suma las fuentes vinculadas a un curso:
     * <ul>
     *   <li>lecciones: {@code usuarioenleccion → leccion → seccion → curso}</li>
     *   <li>exámenes: {@code usuarioenexamen → examen → seccion → curso}</li>
     *   <li>proyectos de curso: {@code usuarioenproyectocurso → proyectocurso.idcurso}</li>
     * </ul>
     * (Proyectos libres y medallas no pertenecen a ningún curso, por eso quedan
     * fuera y solo aparecen en {@code findExpDeTodosLosUsuarios}.)
     *
     * <p>Devuelve todos los cursos, aunque el usuario no tenga progreso en ellos
     * (aparecen con EXP 0), ordenados por id.
     *
     * <p>Cada fila es: {@code [idCurso, nombreCurso, exp]}.
     */
    @Query("""
            SELECT c.idCurso, c.nombre,
                 COALESCE((SELECT SUM(l.puntosLeccion) FROM LeccionEntity l
                        WHERE l.idSeccion.idCurso = c.idCurso
                          AND l.idLeccion IN (SELECT ue.idLeccion FROM UsuarioEnLeccionEntity ue
                                WHERE ue.idUsuario = :idUsuario)), 0L)
                 + COALESCE((SELECT SUM(e.puntosAumento) FROM ExamenEntity e
                        WHERE e.seccion.idCurso = c.idCurso
                          AND e.idExamen IN (SELECT ue.idExamen FROM UsuarioEnExamenEntity ue
                                WHERE ue.idUsuario = :idUsuario)), 0L)
                 + COALESCE((SELECT SUM(pc.puntosProyecto) FROM ProyectoCursoEntity pc
                        WHERE pc.idCurso = c.idCurso
                          AND pc.idProyectoCurso IN (SELECT up.idProyectoCurso FROM UsuarioEnProyectoCursoEntity up
                                WHERE up.idUsuario = :idUsuario)), 0L)
            FROM CursoEntity c
            ORDER BY c.idCurso
            """)
    List<Object[]> findExpPorCursoDeUsuario(@Param("idUsuario") Integer idUsuario);

    /**
     * Ranking de experiencia en UNA sola consulta: calcula la EXP con las
     * mismas cinco subconsultas y une el rol en la propia consulta (sin cargas
     * perezosas por usuario). Excluye administradores, igual que el filtrado
     * anterior.
     *
     * <p>Cada fila es: {@code [idUsuario, nombreUsuario, primerNombre, primerApellido, exp]}.
     */
    @Query("""
            SELECT u.idUsuario, u.nombreUsuario, u.primerNombre, u.primerApellido,
                 COALESCE((SELECT SUM(l.puntosLeccion) FROM LeccionEntity l WHERE l.idLeccion IN
                    (SELECT ue.idLeccion FROM UsuarioEnLeccionEntity ue WHERE ue.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(e.puntosAumento) FROM ExamenEntity e WHERE e.idExamen IN
                    (SELECT ue.idExamen FROM UsuarioEnExamenEntity ue WHERE ue.idUsuario = u.idUsuario)), 0L)
                 + COALESCE((SELECT SUM(pc.puntosProyecto) FROM ProyectoCursoEntity pc WHERE pc.idProyectoCurso IN
                    (SELECT up.idProyectoCurso FROM UsuarioEnProyectoCursoEntity up WHERE up.idUsuario = u.idUsuario)), 0L)
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