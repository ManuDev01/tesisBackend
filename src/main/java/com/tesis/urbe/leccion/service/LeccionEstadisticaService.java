package com.tesis.urbe.leccion.service;

import com.tesis.urbe.leccion.dto.ErrorLeccionDetalleDTO;
import com.tesis.urbe.leccion.dto.TasaErrorLeccionDTO;
import com.tesis.urbe.leccion.dto.TasaErrorSeccionDTO;
import com.tesis.urbe.leccion.dto.TasaErrorUsuarioDTO;
import com.tesis.urbe.leccion.repository.IntentoLeccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeccionEstadisticaService {

    private final IntentoLeccionRepository intentoLeccionRepository;

    public LeccionEstadisticaService(IntentoLeccionRepository intentoLeccionRepository) {
        this.intentoLeccionRepository = intentoLeccionRepository;
    }

    @Transactional(readOnly = true)
    public List<TasaErrorLeccionDTO> getLeccionesConMayorTasaDeError() {
        List<Object[]> filas = intentoLeccionRepository.findTasaErrorPorLeccionRaw();
        List<TasaErrorLeccionDTO> resultado = new ArrayList<>();

        for (Object[] fila : filas) {
            Integer idLeccion = ((Number) fila[0]).intValue();
            String leccion = (String) fila[1];
            String seccion = (String) fila[2];
            Long fallos = ((Number) fila[3]).longValue();
            Long aciertos = ((Number) fila[4]).longValue();
            Double tasaError = fila[5] != null ? ((Number) fila[5]).doubleValue() : 0.0;

            resultado.add(new TasaErrorLeccionDTO(idLeccion, leccion, seccion, fallos, aciertos, tasaError));
        }

        return resultado;
    }

    @Transactional(readOnly = true)
    public List<TasaErrorSeccionDTO> getSeccionesConMayorTasaDeError() {
        List<Object[]> filas = intentoLeccionRepository.findTasaErrorPorSeccionRaw();
        List<TasaErrorSeccionDTO> resultado = new ArrayList<>();

        for (Object[] fila : filas) {
            Integer idSeccion = ((Number) fila[0]).intValue();
            String seccion = (String) fila[1];
            Long fallos = ((Number) fila[2]).longValue();
            Long aciertos = ((Number) fila[3]).longValue();
            Double tasaError = fila[4] != null ? ((Number) fila[4]).doubleValue() : 0.0;

            resultado.add(new TasaErrorSeccionDTO(idSeccion, seccion, fallos, aciertos, tasaError));
        }

        return resultado;
    }

    @Transactional(readOnly = true)
    public List<TasaErrorUsuarioDTO> getTasaErrorPorUsuario() {
        List<Object[]> filas = intentoLeccionRepository.findTasaErrorPorUsuarioRaw();
        List<TasaErrorUsuarioDTO> resultado = new ArrayList<>();

        for (Object[] fila : filas) {
            Integer idUsuario = ((Number) fila[0]).intValue();
            String usuario = (String) fila[1];
            Long aciertos = ((Number) fila[2]).longValue();
            Long fallos = ((Number) fila[3]).longValue();
            Double tasaError = fila[4] != null ? ((Number) fila[4]).doubleValue() : null;
            Long fallosAntesDeResolver = ((Number) fila[5]).longValue();

            List<ErrorLeccionDetalleDTO> erroresPorLeccion = new ArrayList<>();
            String erroresRaw = (String) fila[6];

            if (erroresRaw != null && !erroresRaw.isBlank()) {
                String[] leccionesArr = erroresRaw.split("\\|\\|\\|");
                for (String leccionStr : leccionesArr) {
                    String[] partes = leccionStr.split(":::");
                    if (partes.length == 2 && !partes[0].isBlank() && !partes[1].equals("null")) {
                        erroresPorLeccion.add(new ErrorLeccionDetalleDTO(
                                partes[0],
                                Long.parseLong(partes[1])
                        ));
                    }
                }
            }

            resultado.add(new TasaErrorUsuarioDTO(
                    idUsuario,
                    usuario,
                    aciertos,
                    fallos,
                    tasaError,
                    fallosAntesDeResolver,
                    erroresPorLeccion
            ));
        }

        return resultado;
    }
}