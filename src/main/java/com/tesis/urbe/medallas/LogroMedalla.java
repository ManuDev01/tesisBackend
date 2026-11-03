package com.tesis.urbe.medallas;

/**
 * Catálogo de medallas existentes en la base de datos.
 *
 * <p>Los IDs coinciden con la tabla {@code medallas}. Se usa un enum para evitar
 * números mágicos repartidos por los servicios y para poder recorrer el catálogo
 * completo al comprobar la medalla "Nacido en Java" (todas las demás).
 */
public enum LogroMedalla {

    PROYECTO_PRINCIPIANTE(1),
    DIEZ_PROYECTOS(3),
    FALLO_QUIZZ(4),
    FALLOS_QUIZZ_COMPLETO(5),
    SECCION_COMPLETADA(6),
    LECCION_COMPLETADA(7),
    EXAMEN_PERFECTO(8),
    QUIZZ_PERFECTO(9),
    PROYECTO_UN_INTENTO(10),
    DESAFIOS_FACILES(11),
    DESAFIOS_INTERMEDIOS(12),
    DESAFIOS_DIFICILES(13),
    DESAFIOS_TODOS(14),
    CURSO_BASICO(15),
    CURSO_INTERMEDIO(16),
    CURSO_AVANZADO(17),
    CURSOS_TODOS(18),
    PRIMERA_COMPILACION(19),
    INICIO_CURSO_BASICO(20),
    INICIO_CURSO_INTERMEDIO(21),
    INICIO_CURSO_AVANZADO(22),
    COMPILACION_VACIA(23),
    TODAS_LAS_MEDALLAS(24);

    private final int id;

    LogroMedalla(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
