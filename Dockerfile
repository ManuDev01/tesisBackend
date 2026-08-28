## --- ETAPA 1: Compilación y Construcción ---
#FROM gradle:8-jdk21-alpine AS builder
#
## Crear directorio de trabajo y asegurar permisos correctos
#WORKDIR /home/gradle/project
#COPY --chown=gradle:gradle . .
#
#RUN chmod +x gradlew
## Compilar la aplicación omitiendo las pruebas para acelerar el proceso
#RUN ./gradlew bootJar --no-daemon || ./gradlew build -x test --no-daemon
#
## --- ETAPA 2: Entorno de Ejecución Ligero ---
#FROM eclipse-temurin:21-jdk-alpine
#
#WORKDIR /home/app
#
## Copiar solo el archivo .jar generado desde la etapa de compilación
## Nota: Ajusta 'build/libs/*.jar' si tu proyecto genera el jar en otra ruta
#COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
#
## Exponer el puerto de tu servidor web
#EXPOSE 8080
#
## Comando para arrancar la aplicación Java
#ENTRYPOINT ["java", "-jar", "app.jar"]

# --- ETAPA 1: Compilación ---
FROM gradle:8-jdk21-alpine AS builder

WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

# --- ETAPA 2: Entorno de Ejecución con JDK ---
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /home/app

# Copiar el JAR y la carpeta con las dependencias físicamente extraídas
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
COPY --from=builder /home/gradle/project/build/libs/lib ./lib

EXPOSE 8080

# Iniciar cargando el JAR y la carpeta /lib/* en el Classpath nativo del sistema
ENTRYPOINT ["java", "-cp", "app.jar:lib/*", "org.springframework.boot.loader.launch.JarLauncher"]
