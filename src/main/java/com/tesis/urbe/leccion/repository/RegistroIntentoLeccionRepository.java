package com.tesis.urbe.leccion.repository;

import com.tesis.urbe.leccion.entity.RegistroIntentoLeccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Acceso a la tabla {@code intento_leccion} (intento de lección con solo
 * idUsuario + idLeccion).
 *
 * <p>No confundir con {@link IntentoLeccionRepository}, que opera sobre la
 * tabla distinta {@code intentoleccion} (con escorrecto/idseccion).
 */
@Repository
public interface RegistroIntentoLeccionRepository extends JpaRepository<RegistroIntentoLeccionEntity, Integer> {

    List<RegistroIntentoLeccionEntity> findByIdUsuario(Integer idUsuario);

    /**
     * Conteo total de intentos por lección desde la tabla
     * {@code intento_leccion}: id de la lección, título y número de intentos.
     *
     * <p>Una sola consulta agrupada (cero N+1). El cruce con
     * {@code leccion} se hace con la sintaxis de join implícito
     * ({@code FROM a, b WHERE a.x = b.x}), así que solo aparecen las
     * lecciones con al menos un intento, de mayor a menor conteo.
     *
     * <p>Cada fila es: {@code [idLeccion, titulo, totalIntentos]}.
     */
    @Query("""
            SELECT i.idLeccion, l.titulo, COUNT(i.idIntentoLeccion)
            FROM RegistroIntentoLeccionEntity i, LeccionEntity l
            WHERE l.idLeccion = i.idLeccion
            GROUP BY i.idLeccion, l.titulo
            ORDER BY COUNT(i.idIntentoLeccion) DESC, i.idLeccion ASC
            """)
    List<Object[]> countIntentosPorLeccion();
}
