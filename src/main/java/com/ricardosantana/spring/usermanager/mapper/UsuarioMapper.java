package com.ricardosantana.spring.usermanager.mapper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ricardosantana.spring.usermanager.dtos.UsuarioDTO;
import com.ricardosantana.spring.usermanager.dtos.UsuarioPageDTO;
import com.ricardosantana.spring.usermanager.models.Usuario;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario(usuarioDTO.id(), usuarioDTO.dataHoraCadastro(),
                usuarioDTO.usuarioCadastrado(), usuarioDTO.nome(), usuarioDTO.email(),
                usuarioDTO.telefone());
        return usuario;
    }

    public UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getDataHoraCadastro(), usuario.getUsuarioCadastrado(),
                usuario.getNome(), usuario.getEmail(), usuario.getTelefone());
    }

    public UsuarioPageDTO toPageDTO(List<UsuarioDTO> usuariosDto, Page<Usuario> pageUsuario) {
        return new UsuarioPageDTO(usuariosDto, pageUsuario.getTotalElements(),
                pageUsuario.getTotalPages());
    }
}
