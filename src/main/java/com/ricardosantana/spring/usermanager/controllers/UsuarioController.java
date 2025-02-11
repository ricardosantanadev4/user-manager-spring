package com.ricardosantana.spring.usermanager.controllers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ricardosantana.spring.usermanager.dtos.UsuarioDTO;
import com.ricardosantana.spring.usermanager.dtos.UsuarioPageDTO;
import com.ricardosantana.spring.usermanager.models.Usuario;
import com.ricardosantana.spring.usermanager.services.UsuarioService;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/criar")
    public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody UsuarioDTO usuarioDto) {
        UsuarioDTO body = this.usuarioService.criarUsuario(usuarioDto);
        return new ResponseEntity<UsuarioDTO>(body, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UsuarioPageDTO> listarUsuariosPaginados(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search,
            @RequestParam(required = false) LocalDateTime dataInicial,
            @RequestParam(required = false) LocalDateTime dataFinal) {
        UsuarioPageDTO usuarioPageDTO = this.usuarioService.listarUsuariosPaginados(page, size, search,
                dataInicial, dataFinal);
        return new ResponseEntity<UsuarioPageDTO>(usuarioPageDTO, HttpStatus.OK);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorID(@PathVariable Long usuarioId) {
        Usuario usuario = this.usuarioService.buscarUsuarioPorId(usuarioId);
        UsuarioDTO usuarioDto = this.usuarioService.toDto(usuario);
        return new ResponseEntity<UsuarioDTO>(usuarioDto, HttpStatus.OK);
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long usuarioId,
            @RequestBody UsuarioDTO usuarioDto) {
        UsuarioDTO body = this.usuarioService.atualizarUsuario(usuarioId, usuarioDto);
        return new ResponseEntity<UsuarioDTO>(body, HttpStatus.OK);
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> removerUsuario(@PathVariable Long usuarioId) {
        this.usuarioService.removerUsuario(usuarioId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}