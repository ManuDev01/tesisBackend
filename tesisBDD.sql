-- 1. Tabla: Rol
CREATE TABLE rol (
                     idRol SERIAL PRIMARY KEY,
                     nombreRol VARCHAR(50) NOT NULL
);

-- 2. Tabla: Seccion
CREATE TABLE seccion (
                         idSeccion SERIAL PRIMARY KEY,
                         titulo VARCHAR(100) NOT NULL,
                         descripcion TEXT,
                         estado VARCHAR(20),
                         puntosSeccion INT DEFAULT 0
);

-- 3. Tabla: Usuarios
CREATE TABLE usuarios (
                          idUsuario SERIAL PRIMARY KEY,
                          primerNombre VARCHAR(50) NOT NULL,
                          segundoNombre VARCHAR(50),
                          primerApellido VARCHAR(50) NOT NULL,
                          segundoApellido VARCHAR(50),
                          nombreUsuario VARCHAR(50) NOT NULL UNIQUE,
                          cedula VARCHAR(20) NOT NULL UNIQUE,
                          correo VARCHAR(100) NOT NULL UNIQUE,
                          contrasena VARCHAR(255) NOT NULL,
                          idRol INT NOT NULL,
                          activo BOOLEAN DEFAULT TRUE,
                          CONSTRAINT fk_usuarios_rol FOREIGN KEY (idRol) REFERENCES rol(idRol)
);

-- 4. Tabla: Leccion
CREATE TABLE leccion (
                         idLeccion SERIAL PRIMARY KEY,
                         idSeccion INT NOT NULL,
                         titulo VARCHAR(100) NOT NULL,
                         descripcion TEXT,
                         estado VARCHAR(20),
                         puntosLeccion INT DEFAULT 0,
                         CONSTRAINT fk_leccion_seccion FOREIGN KEY (idSeccion) REFERENCES seccion(idSeccion)
);

-- 5. Tabla: CasosDeUso
-- Nota: La creamos antes de Proyectos porque Proyectos hace referencia a idCasoDeUso
CREATE TABLE casosDeUso (
                            idCasoDeUso SERIAL PRIMARY KEY,
                            idProyecto INT -- Se puede enlazar mediante FK después o dejar como campo entero
);

-- 6. Tabla: Proyectos
CREATE TABLE proyectos (
                           idProyecto SERIAL PRIMARY KEY,
                           idSeccion INT, -- En la pizarra dice 'idSecciom Fk Null'
                           titulo VARCHAR(100) NOT NULL,
                           descripcion TEXT,
                           codigo VARCHAR(50),
                           estado VARCHAR(20),
                           puntosProyectos INT DEFAULT 0,
                           idCasoDeUso INT,
                           createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_proyectos_seccion FOREIGN KEY (idSeccion) REFERENCES seccion(idSeccion),
                           CONSTRAINT fk_proyectos_casodeuso FOREIGN KEY (idCasoDeUso) REFERENCES casosDeUso(idCasoDeUso)
);

-- Ahora agregamos la llave foránea que faltaba en CasosDeUso hacia Proyectos
ALTER TABLE casosDeUso
    ADD CONSTRAINT fk_casosdeuso_proyecto FOREIGN KEY (idProyecto) REFERENCES proyectos(idProyecto);

-- 7. Tabla intermedia: UsuarioEnLeccion
-- (Representa la relación de asignación o progreso)
CREATE TABLE usuarioEnLeccion (
                                  idSeccion INT,
                                  idUsuario INT,
                                  PRIMARY KEY (idSeccion, idUsuario),
                                  CONSTRAINT fk_userleccion_seccion FOREIGN KEY (idSeccion) REFERENCES seccion(idSeccion),
                                  CONSTRAINT fk_userleccion_usuario FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario)
);