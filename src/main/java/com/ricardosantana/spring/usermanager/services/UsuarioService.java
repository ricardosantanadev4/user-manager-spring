package com.ricardosantana.spring.usermanager.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ricardosantana.spring.usermanager.dtos.UsuarioDTO;
import com.ricardosantana.spring.usermanager.dtos.UsuarioPageDTO;
import com.ricardosantana.spring.usermanager.exceptions.CustomIllegalArgumentException;
import com.ricardosantana.spring.usermanager.exceptions.CustomObjectNotFoundException;
import com.ricardosantana.spring.usermanager.mapper.UsuarioMapper;
import com.ricardosantana.spring.usermanager.models.Usuario;
import com.ricardosantana.spring.usermanager.repositorys.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper,
            BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDto) {
        this.buscarUsuarioPorEmail(usuarioDto.email());
        Usuario toEntity = this.usuarioMapper.toEntity(usuarioDto);
        Usuario usuarioDataAtual = this.adicionarDataAtual(toEntity);
        Usuario usuarioSenhaCodificada = this.codificarSenha(usuarioDataAtual);
        Usuario usuario = this.usuarioRepository.save(usuarioSenhaCodificada);
        UsuarioDTO toDto = this.usuarioMapper.toDTO(usuario);
        return toDto;
    }

    public boolean buscarUsuarioPorEmail(String email) {
        Optional<Usuario> optionalUsuario = this.usuarioRepository.findByEmail(email);

        if (optionalUsuario.isPresent() && optionalUsuario.get().getEmail().equals(email)) {
            throw new CustomIllegalArgumentException("Usuário já cadastrado com o email: " + email);
        }
        return true;
    }

    public Usuario adicionarDataAtual(Usuario entity) {
        Usuario usuario = new Usuario();
        usuario.setId(entity.getId());
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        usuario.setDataHoraCadastro(dataHoraAtual);
        usuario.setUsuarioCadastrado(entity.getUsuarioCadastrado());
        usuario.setNome(entity.getNome());
        usuario.setEmail(entity.getEmail());
        usuario.setSenha(entity.getSenha());
        usuario.setTelefone(entity.getTelefone());
        usuario.setRole(entity.getRole());
        return usuario;
    }

    public Usuario codificarSenha(Usuario entity) {
        Usuario usuario = new Usuario();
        usuario.setId(entity.getId());
        usuario.setDataHoraCadastro(entity.getDataHoraCadastro());
        usuario.setUsuarioCadastrado(entity.getUsuarioCadastrado());
        usuario.setNome(entity.getNome());
        usuario.setEmail(entity.getEmail());
        usuario.setSenha(passwordEncoder.encode(entity.getSenha()));
        usuario.setTelefone(entity.getTelefone());
        usuario.setRole(entity.getRole());
        return usuario;
    }

    public UsuarioPageDTO listarUsuariosPaginados(int page, int size, String search,
            LocalDateTime dataInicial, LocalDateTime dataFinal) {

        if (!this.verficarDataNula(dataInicial) && !this.verficarDataNula(dataFinal)) {
            LocalDateTime inicioDoDia = dataInicial.toLocalDate().atStartOfDay(); // 2025-02-12T00:00:00
            LocalDateTime fimDoDia = dataFinal.toLocalDate().atTime(23, 59, 59, 999999999); // 2025-02-28T23:59:59.999999999
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            Page<Usuario> pageUsuario = this.usuarioRepository.findByDataHoraBetween(inicioDoDia,
                    fimDoDia, pageable);
            UsuarioPageDTO usuarioPageDTO = this.paginarUsuariosEmDTO(pageUsuario);
            return usuarioPageDTO;
        }

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

    public boolean verficarDataNula(LocalDateTime data) {
        return data == null;
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
            throw new CustomObjectNotFoundException("Usuário não encontrado com id: " + usuarioId);
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