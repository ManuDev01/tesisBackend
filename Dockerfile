# --- ETAPA 1: Compilación y Construcción ---
FROM gradle:8-jdk21-alpine AS builder

# Crear directorio de trabajo y asegurar permisos correctos
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .

RUN chmod +x gradlew
# Compilar la aplicación omitiendo las pruebas para acelerar el proceso
RUN ./gradlew bootJar --no-daemon || ./gradlew build -x test --no-daemon

# --- ETAPA 2: Entorno de Ejecución Ligero ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /home/app

# Copiar solo el archivo .jar generado desde la etapa de compilación
# Nota: Ajusta 'build/libs/*.jar' si tu proyecto genera el jar en otra ruta
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

# Exponer el puerto de tu servidor web
EXPOSE 8080

# Comando para arrancar la aplicación Java
ENTRYPOINT ["java", "-jar", "app.jar"]
