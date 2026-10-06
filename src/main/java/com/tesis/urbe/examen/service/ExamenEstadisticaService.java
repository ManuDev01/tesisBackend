package com.tesis.urbe.examen.service;

import com.tesis.urbe.examen.dto.ExamenAntesDeSeccionDTO;
import com.tesis.urbe.examen.repository.UsuarioEnExamenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamenEstadisticaService {

    private final UsuarioEnExamenRepository usuarioEnExamenRepository;

    public ExamenEstadisticaService(UsuarioEnExamenRepository usuarioEnExamenRepository) {
        this.usuarioEnExamenRepository = usuarioEnExamenRepository;
    }

    @Transactional(readOnly = true)
    public List<ExamenAntesDeSeccionDTO> getUsuariosExamenAntesDeCompletarSeccion() {
        List<Object[]> filas = usuarioEnExamenRepository.findUsuariosConExamenIncompletoRaw();
        List<ExamenAntesDeSeccionDTO> resultado = new ArrayList<>();

        for (Object[] fila : filas) {
            Integer idUsuario = ((Number) fila[0]).intValue();
            String usuario = (String) fila[1];
            String seccion = (String) fila[2];
            Long leccionesCompletadas = ((Number) fila[3]).longValue();
            Long totalLecciones = ((Number) fila[4]).longValue();
            String pendientesRaw = (String) fila[5];

            List<ExamenAntesDeSeccionDTO.LeccionPendienteDTO> detallePendientes = new ArrayList<>();

            if (pendientesRaw != null && !pendientesRaw.isBlank()) {
                String[] leccionesArray = pendientesRaw.split("\\|\\|\\|");
                for (String item : leccionesArray) {
                    String[] partes = item.split(":::");
                    if (partes.length == 2) {
                        detallePendientes.add(new ExamenAntesDeSeccionDTO.LeccionPendienteDTO(
                                Integer.parseInt(partes[0]),
                                partes[1]
                        ));
                    }
                }
            }

            Long pendientesCount = (long) detallePendientes.size();

            resultado.add(new ExamenAntesDeSeccionDTO(
                    idUsuario,
                    usuario,
                    seccion,
                    leccionesCompletadas,
                    totalLecciones,
                    pendientesCount,
                    detallePendientes
            ));
        }

        return resultado;
    }
}