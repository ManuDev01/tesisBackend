package com.tesis.urbe.certificados.controller;

import com.tesis.urbe.certificados.dto.CertificadoResponseDTO;
import com.tesis.urbe.certificados.dto.GuardarCertificadoDTO;
import com.tesis.urbe.certificados.service.CertificadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificados")
public class CertificadoController {

    private final CertificadoService certificadoService;

    public CertificadoController(CertificadoService certificadoService) {
        this.certificadoService = certificadoService;
    }

    @PostMapping("/emitir")
    public ResponseEntity<CertificadoResponseDTO> emitirCertificado(@RequestBody GuardarCertificadoDTO dto) {
        CertificadoResponseDTO certificado = certificadoService.guardarCertificado(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(certificado);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<CertificadoResponseDTO>> getCertificadosByUsuario(@PathVariable Integer idUsuario) {
        List<CertificadoResponseDTO> certificados = certificadoService.getCertificadosByUsuario(idUsuario);
        return ResponseEntity.ok(certificados);
    }
}