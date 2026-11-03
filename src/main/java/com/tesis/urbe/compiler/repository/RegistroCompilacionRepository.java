package com.tesis.urbe.compiler.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Locale;

/**
 * Registro ligero de compilaciones para poder verificar las medallas que
 * dependen del historial de intentos (por ejemplo "Speedrunner del Código").
 *
 * <p>Se usa JDBC directo en lugar de una entidad JPA para que el proyecto
 * arranque sin necesidad de ejecutar una migración manual: la tabla se crea de
 * forma perezosa la primera vez que se necesita. Si el usuario de base de datos
 * no tiene permisos de creación, el registro se desactiva y el resto de la
 * aplicación sigue funcionando con normalidad.
 */
@Repository
public class RegistroCompilacionRepository {

    private static final Logger log = LoggerFactory.getLogger(RegistroCompilacionRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private volatile boolean tablaDisponible = false;

    public RegistroCompilacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registrar(Integer idUsuario,
                          String tipoProyecto,
                          Integer idReferencia,
                          boolean exitoso,
                          boolean codigoVacio) {
        if (idUsuario == null) {
            return;
        }

        try {
            asegurarTabla();
            jdbcTemplate.update(
                    "INSERT INTO intentocompilacion "
                            + "(idusuario, tipoproyecto, idreferencia, exitoso, codigovacio) "
                            + "VALUES (?, ?, ?, ?, ?)",
                    idUsuario, normalizarTipo(tipoProyecto), idReferencia, exitoso, codigoVacio);
        } catch (Exception e) {
            invalidarTabla(e, "registrar la compilación del usuario " + idUsuario);
        }
    }

    public long contarIntentos(Integer idUsuario, String tipoProyecto, Integer idReferencia) {
        if (idUsuario == null || tipoProyecto == null || idReferencia == null) {
            return 0L;
        }

        try {
            asegurarTabla();
            Long total = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM intentocompilacion "
                            + "WHERE idusuario = ? AND tipoproyecto = ? AND idreferencia = ?",
                    Long.class, idUsuario, normalizarTipo(tipoProyecto), idReferencia);
            return total != null ? total : 0L;
        } catch (Exception e) {
            invalidarTabla(e, "contar las compilaciones del usuario " + idUsuario);
            return 0L;
        }
    }

    public long contarIntentosExitosos(Integer idUsuario, String tipoProyecto, Integer idReferencia) {
        if (idUsuario == null || tipoProyecto == null || idReferencia == null) {
            return 0L;
        }

        try {
            asegurarTabla();
            Long total = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM intentocompilacion "
                            + "WHERE idusuario = ? AND tipoproyecto = ? AND idreferencia = ? AND exitoso = TRUE",
                    Long.class, idUsuario, normalizarTipo(tipoProyecto), idReferencia);
            return total != null ? total : 0L;
        } catch (Exception e) {
            invalidarTabla(e, "contar las compilaciones exitosas del usuario " + idUsuario);
            return 0L;
        }
    }

    public long contarCompilaciones(Integer idUsuario) {
        if (idUsuario == null) {
            return 0L;
        }

        try {
            asegurarTabla();
            Long total = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM intentocompilacion WHERE idusuario = ? AND exitoso = TRUE",
                    Long.class, idUsuario);
            return total != null ? total : 0L;
        } catch (Exception e) {
            invalidarTabla(e, "contar las compilaciones exitosas del usuario " + idUsuario);
            return 0L;
        }
    }

    public boolean existeCompilacionVacia(Integer idUsuario) {
        if (idUsuario == null) {
            return false;
        }

        try {
            asegurarTabla();
            Long total = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM intentocompilacion WHERE idusuario = ? AND codigovacio = TRUE",
                    Long.class, idUsuario);
            return total != null && total > 0;
        } catch (Exception e) {
            invalidarTabla(e, "verificar la compilación vacía del usuario " + idUsuario);
            return false;
        }
    }

    private void asegurarTabla() {
        if (tablaDisponible) {
            return;
        }

        synchronized (this) {
            if (tablaDisponible) {
                return;
            }

            jdbcTemplate.execute("""
                    CREATE TABLE IF NOT EXISTS intentocompilacion (
                        idintentocompilacion SERIAL PRIMARY KEY,
                        idusuario INTEGER NOT NULL,
                        tipoproyecto VARCHAR(20),
                        idreferencia INTEGER,
                        exitoso BOOLEAN NOT NULL DEFAULT FALSE,
                        codigovacio BOOLEAN NOT NULL DEFAULT FALSE,
                        createdat TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
            jdbcTemplate.execute("""
                    CREATE INDEX IF NOT EXISTS ix_intentocompilacion_usuario_referencia
                    ON intentocompilacion (idusuario, tipoproyecto, idreferencia)
                    """);
            tablaDisponible = true;
        }
    }

    private void invalidarTabla(Exception e, String contexto) {
        tablaDisponible = false;
        log.warn("No se pudo {}: {}", contexto, e.getMessage());
    }

    private String normalizarTipo(String tipoProyecto) {
        return tipoProyecto == null ? null : tipoProyecto.trim().toUpperCase(Locale.ROOT);
    }
}
