package com.ricardosantana.spring.usermanager.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ricardosantana.spring.usermanager.dtos.UsuarioDTO;
import com.ricardosantana.spring.usermanager.dtos.UsuarioPageDTO;
import com.ricardosantana.spring.usermanager.mapper.UsuarioMapper;
import com.ricardosantana.spring.usermanager.models.Usuario;
import com.ricardosantana.spring.usermanager.repositories.UsuarioRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDto) {
        this.buscarUsuarioPorEmail(usuarioDto.email());
        Usuario toEntity = this.usuarioMapper.toEntity(usuarioDto);
        Usuario usuario = this.usuarioRepository.save(toEntity);
        UsuarioDTO toDto = this.usuarioMapper.toDTO(usuario);
        return toDto;
    }

    public boolean buscarUsuarioPorEmail(String email) {
        Optional<Usuario> optionalUsuario = this.usuarioRepository.findByEmail(email);

        if (optionalUsuario.isPresent() && optionalUsuario.get().getEmail().equals(email)) {
            throw new EntityExistsException("Usuário já cadastrado com o email: " + email);
        }
        return true;
    }

    public UsuarioPageDTO listarUsuariosPaginados(int page, int size, String search) {

        if (this.verificarStringVaziaOuNula(search)) {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            Page<Usuario> pageUsuario = this.usuarioRepository.findAll(pageable);
            UsuarioPageDTO usuarioPageDTO = this.paginarUsuariosEmDTO(pageUsuario);
            return usuarioPageDTO;
        }
        Pageable pageable = PageRequest.of(0, size, Sort.Direction.ASC, "id");
        Page<Usuario> pageUsuario = this.usuarioRepository.searchUsers(search,
                pageable);
        UsuarioPageDTO usuarioPageDTO = this.paginarUsuariosEmDTO(pageUsuario);
        return usuarioPageDTO;
    }

    public boolean verificarStringVaziaOuNula(String str) {
        return str == null || str.trim().isEmpty();
    }

    public UsuarioPageDTO paginarUsuariosEmDTO(Page<Usuario> pageUsuario) {
        List<UsuarioDTO> usuariosDto = pageUsuario.get().map(usuario -> this.usuarioMapper
                .toDTO(usuario)).collect(Collectors.toList());
        UsuarioPageDTO usuarioPageDTO = this.usuarioMapper.toPageDTO(usuariosDto, pageUsuario);
        return usuarioPageDTO;
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDto) {
        Usuario result = this.buscarUsuarioPorId(id);
        result.setId(result.getId());
        result.setEmail(usuarioDto.email());
        result.setNome(usuarioDto.nome());
        result.setTelefone(usuarioDto.telefone());
        Usuario usuario = this.usuarioRepository.save(result);
        UsuarioDTO toDto = this.usuarioMapper.toDTO(usuario);
        return toDto;
    }

    public Usuario buscarUsuarioPorId(Long usuarioId) {
        Optional<Usuario> optionalUsuario = this.usuarioRepository.findById(usuarioId);
        if (!optionalUsuario.isPresent()) {
            throw new EntityNotFoundException("Usuário não encontrado com id: " + usuarioId);
        }
        Usuario usuario = optionalUsuario.get();
        return usuario;
    }

    public void removerUsuario(Long usuarioId) {
        Usuario usuario = this.buscarUsuarioPorId(usuarioId);
        this.usuarioRepository.deleteById(usuario.getId());
    }

    public UsuarioDTO toDto(Usuario usuario) {
        UsuarioDTO usuarioDto = this.usuarioMapper.toDTO(usuario);
        return usuarioDto;
    }
}