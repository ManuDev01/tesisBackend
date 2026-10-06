package com.tesis.urbe.leccion.repository;

import com.tesis.urbe.leccion.entity.IntentoLeccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntentoLeccionRepository extends JpaRepository<IntentoLeccionEntity, Integer> {

    @Query(value = """
        SELECT 
            l.idleccion AS idLeccion,
            l.titulo AS leccion,
            CONCAT(s.titulo, ' de ', c.nombre) AS seccion,
            COUNT(CASE WHEN il.escorrecto = false THEN 1 END) AS fallos,
            COUNT(CASE WHEN il.escorrecto = true THEN 1 END) AS aciertos,
            ROUND(
                (COUNT(CASE WHEN il.escorrecto = false THEN 1 END)::numeric / COUNT(il.idintentoleccion)::numeric) * 100, 
                1
            ) AS tasaError
        FROM public.intentoleccion il
        INNER JOIN public.leccion l ON il.idleccion = l.idleccion
        INNER JOIN public.seccion s ON il.idseccion = s.idseccion
        INNER JOIN public.curso c ON s.idcurso = c.idcurso
        GROUP BY l.idleccion, l.titulo, s.titulo, c.nombre
        ORDER BY tasaError DESC, fallos DESC
        """, nativeQuery = true)
    List<Object[]> findTasaErrorPorLeccionRaw();

    @Query(value = """
    SELECT 
        s.idseccion AS idSeccion,
        CONCAT(s.titulo, ' de ', c.nombre) AS seccion,
        COUNT(CASE WHEN il.escorrecto = false THEN 1 END) AS fallos,
        COUNT(CASE WHEN il.escorrecto = true THEN 1 END) AS aciertos,
        CASE 
            WHEN COUNT(il.idintentoleccion) = 0 THEN 0.0
            ELSE ROUND(
                (COUNT(CASE WHEN il.escorrecto = false THEN 1 END)::numeric / COUNT(il.idintentoleccion)::numeric) * 100, 
                1
            )
        END AS tasaError
    FROM public.seccion s
    INNER JOIN public.curso c ON s.idcurso = c.idcurso
    LEFT JOIN public.intentoleccion il ON s.idseccion = il.idseccion
    GROUP BY s.idseccion, s.titulo, c.nombre
    ORDER BY tasaError DESC NULLS LAST, fallos DESC
    """, nativeQuery = true)
    List<Object[]> findTasaErrorPorSeccionRaw();

    @Query(value = """
        SELECT 
            u.idusuario AS idUsuario,
            TRIM(CONCAT(u.primernombre, ' ', u.primerapellido)) AS usuario,
            COUNT(CASE WHEN il.escorrecto = true THEN 1 END) AS aciertos,
            COUNT(CASE WHEN il.escorrecto = false THEN 1 END) AS fallos,
            ROUND(
                (COUNT(CASE WHEN il.escorrecto = false THEN 1 END)::numeric / NULLIF(COUNT(il.idintentoleccion), 0)::numeric) * 100, 
                1
            ) AS tasaError,
            COUNT(DISTINCT CASE WHEN primer_intento.escorrecto = false THEN primer_intento.idleccion END) AS fallosAntesDeResolver,
            ARRAY_TO_STRING(
                ARRAY_AGG(DISTINCT CONCAT(l.titulo, ':::', sub_fallos.total_fallos)), 
                '|||'
            ) AS erroresPorLeccionRaw
        FROM public.usuarios u
        LEFT JOIN public.intentoleccion il ON u.idusuario = il.idusuario
        LEFT JOIN public.leccion l ON il.idleccion = l.idleccion
        LEFT JOIN (
            SELECT DISTINCT ON (idusuario, idleccion) idusuario, idleccion, escorrecto
            FROM public.intentoleccion
            ORDER BY idusuario, idleccion, createdat ASC
        ) primer_intento ON u.idusuario = primer_intento.idusuario AND il.idleccion = primer_intento.idleccion
        LEFT JOIN (
            SELECT idusuario, idleccion, COUNT(*) AS total_fallos
            FROM public.intentoleccion
            WHERE escorrecto = false
            GROUP BY idusuario, idleccion
        ) sub_fallos ON u.idusuario = sub_fallos.idusuario AND il.idleccion = sub_fallos.idleccion
        GROUP BY u.idusuario, u.primernombre, u.primerapellido
        ORDER BY u.primernombre ASC
        """, nativeQuery = true)
    List<Object[]> findTasaErrorPorUsuarioRaw();
}