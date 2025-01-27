package com.ricardosantana.spring.usermanager.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ricardosantana.spring.usermanager.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE " +
            "LOWER(u.nome) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.telefone) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "CAST(u.id AS string) LIKE CONCAT('%', :search, '%')")
    Page<Usuario> searchUsers(@Param("search") String search, Pageable pageable);
}
