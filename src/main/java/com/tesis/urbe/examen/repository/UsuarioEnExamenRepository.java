package com.tesis.urbe.examen.repository;

import com.tesis.urbe.examen.entity.ExamenEntity;
import com.tesis.urbe.examen.entity.UsuarioEnExamenEntity; // Ajusta el paquete según corresponda
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioEnExamenRepository extends ListCrudRepository<UsuarioEnExamenEntity, Integer> {
    boolean existsByIdUsuarioAndIdExamen(Integer idUsuario, Integer idExamen);
    List<UsuarioEnExamenEntity> findByIdUsuario(Integer idUsuario);
    @Query("SELECT e FROM ExamenEntity e JOIN FETCH e.seccion WHERE e.idExamen = :idExamen")
    Optional<ExamenEntity> findExamenWithSeccionById(@Param("idExamen") Integer idExamen);

    @Query(value = """
        SELECT 
            u.idusuario AS idUsuario,
            TRIM(CONCAT(u.primernombre, ' ', u.primerapellido)) AS usuario,
            CONCAT(s.titulo, ' de ', c.nombre) AS seccion,
            (
                SELECT COUNT(DISTINCT l.idleccion)
                FROM public.leccion l
                INNER JOIN public.usuarioenleccion ul ON l.idleccion = ul.idleccion
                WHERE l.idseccion = s.idseccion AND ul.idusuario = u.idusuario
            ) AS leccionesCompletadas,
            (
                SELECT COUNT(l.idleccion)
                FROM public.leccion l
                WHERE l.idseccion = s.idseccion
            ) AS totalLecciones,
            ARRAY_TO_STRING(ARRAY(
                SELECT CONCAT(l.idleccion, ':::', l.titulo)
                FROM public.leccion l
                WHERE l.idseccion = s.idseccion
                  AND l.idleccion NOT IN (
                      SELECT ul.idleccion 
                      FROM public.usuarioenleccion ul 
                      WHERE ul.idusuario = u.idusuario
                  )
            ), '|||') AS pendientesRaw
        FROM public.usuarioenexamen ue
        INNER JOIN public.usuarios u ON ue.idusuario = u.idusuario
        INNER JOIN public.examen e ON ue.idexamen = e.idexamen
        INNER JOIN public.seccion s ON e.idseccion = s.idseccion
        INNER JOIN public.curso c ON s.idcurso = c.idcurso
        GROUP BY u.idusuario, u.primernombre, u.primerapellido, s.idseccion, s.titulo, c.nombre
        HAVING (
            SELECT COUNT(l.idleccion)
            FROM public.leccion l
            WHERE l.idseccion = s.idseccion
              AND l.idleccion NOT IN (
                  SELECT ul.idleccion 
                  FROM public.usuarioenleccion ul 
                  WHERE ul.idusuario = u.idusuario
              )
        ) > 0
        """, nativeQuery = true)
    List<Object[]> findUsuariosConExamenIncompletoRaw();

}