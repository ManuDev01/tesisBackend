package com.tesis.urbe.leccion.repository;

import com.tesis.urbe.leccion.entity.UsuarioEnLeccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioEnLeccionRepository extends JpaRepository<UsuarioEnLeccionEntity, Integer> {

    // Método para verificar si el usuario ya completó la lección antes de insertar
    boolean existsByIdLeccionAndIdUsuario(Integer idLeccion, Integer idUsuario);

    // Método para consultar todas las lecciones completadas de un usuario
    List<UsuarioEnLeccionEntity> findByIdUsuario(Integer idUsuario);

    @Query("SELECT ul.idLeccion FROM UsuarioEnLeccionEntity ul WHERE ul.idUsuario = :idUsuario")
    List<Integer> findIdsLeccionesCompletadasByUsuario(@Param("idUsuario") Integer idUsuario);

    @Query(value = """
        SELECT 
            u.idusuario AS idUsuario,
            TRIM(CONCAT(u.primernombre, ' ', u.primerapellido)) AS usuario,
            COUNT(DISTINCT ul.idleccion) AS leccionesCompletadas,
            ARRAY_TO_STRING(ARRAY_AGG(DISTINCT CONCAT(l.idleccion, ':::', l.titulo)), '|||') AS leccionesRaw
        FROM public.usuarios u
        LEFT JOIN public.usuarioenleccion ul ON u.idusuario = ul.idusuario
        LEFT JOIN public.leccion l ON ul.idleccion = l.idleccion
        GROUP BY u.idusuario, u.primernombre, u.primerapellido
        ORDER BY u.primernombre ASC
        """, nativeQuery = true)
    List<Object[]> findResumenLeccionesCompletadasPorUsuarioRaw();
}