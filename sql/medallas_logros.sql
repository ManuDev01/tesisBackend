-- =====================================================================
-- Medallas: soporte de verificación automática
-- =====================================================================
-- Este script es OPCIONAL. La tabla intentocompilacion se crea de forma
-- perezosa desde la aplicación (RegistroCompilacionRepository) para que el
-- backend arranque sin pasos manuales. Se deja aquí para entornos donde el
-- usuario de base de datos no tenga permisos de CREATE TABLE.
-- =====================================================================

-- Historial de compilaciones (medallas 10, 19 y 23)
CREATE TABLE IF NOT EXISTS intentocompilacion (
    idintentocompilacion SERIAL PRIMARY KEY,
    idusuario INTEGER NOT NULL,
    tipoproyecto VARCHAR(20),
    idreferencia INTEGER,
    exitoso BOOLEAN NOT NULL DEFAULT FALSE,
    codigovacio BOOLEAN NOT NULL DEFAULT FALSE,
    createdat TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS ix_intentocompilacion_usuario_referencia
    ON intentocompilacion (idusuario, tipoproyecto, idreferencia);

-- Evita que un usuario reciba dos veces la misma medalla.
-- Si la tabla ya contiene duplicados se muestra un aviso y no se crea el índice.
DO $$
BEGIN
    CREATE UNIQUE INDEX IF NOT EXISTS ux_usuarioconmedalla_usuario_medalla
        ON usuarioconmedalla (idusuario, idmedalla);
EXCEPTION WHEN unique_violation THEN
    RAISE NOTICE 'No se creó ux_usuarioconmedalla_usuario_medalla: existen medallas duplicadas.';
END $$;

-- Índices de apoyo para las verificaciones de logros.
CREATE INDEX IF NOT EXISTS ix_usuarioenleccion_usuario
    ON usuarioenleccion (idusuario);

CREATE INDEX IF NOT EXISTS ix_usuarioenexamen_usuario
    ON usuarioenexamen (idusuario);

CREATE INDEX IF NOT EXISTS ix_usuarioenproyectocurso_usuario
    ON usuarioenproyectocurso (idusuario);

CREATE INDEX IF NOT EXISTS ix_usuarioconproyecto_usuario
    ON usuarioconproyecto (idusuario);
