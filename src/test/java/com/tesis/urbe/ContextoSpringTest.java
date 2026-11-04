package com.tesis.urbe;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Arranca el contexto completo de Spring contra una BD H2 en memoria para
 * validar, sin depender de la BD remota, que:
 *
 * <ul>
 *   <li>todas las inyecciones de dependencias de los services/controllers
 *       (LogroService, QuizzService, CompilerController, etc.) estén
 *       resueltas;</li>
 *   <li>las queries JPQL de los repositorios (incluida
 *       {@code IntentoLeccionRepository.contarFallosDeLeccion}) sean
 *       válidas, ya que Spring Data las valida al crear los proxies.</li>
 * </ul>
 *
 * <p>{@code ddl-auto=none} evita generar/esquematizar contra H2 (las
 * columnDefinition tipo {@code bpchar} son de PostgreSQL); la validación de
 * JPQL se hace contra el metamodelo de entidades, no contra la BD.
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:tesis;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.jpa.show-sql=false"
})
class ContextoSpringTest {

    @Test
    void contextLoads() {
    }
}
