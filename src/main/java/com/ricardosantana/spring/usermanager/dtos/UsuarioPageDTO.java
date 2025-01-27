package com.ricardosantana.spring.usermanager.dtos;

import java.util.List;

public record UsuarioPageDTO(List<UsuarioDTO> usuarios, long totalElements, int totalPages) {

}