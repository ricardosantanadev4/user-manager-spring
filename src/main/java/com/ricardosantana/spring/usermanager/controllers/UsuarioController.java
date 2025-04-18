package com.ricardosantana.spring.usermanager.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.ricardosantana.spring.usermanager.repositorys.UsuarioRepository;
import com.ricardosantana.spring.usermanager.services.ExcelExportService;
import com.ricardosantana.spring.usermanager.services.PdfExportService;
import com.ricardosantana.spring.usermanager.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("usuarios")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuários", description = "Gerenciamento de usuários na aplicação")
@Validated
public class UsuarioController {

        private final UsuarioService usuarioService;
        private final ExcelExportService excelExport;
        private final UsuarioRepository usuarioRepository;
        private final PdfExportService pdfExportService;

        public UsuarioController(UsuarioService usuarioService, ExcelExportService excelExport,
                        UsuarioRepository usuarioRepository, PdfExportService pdfExportService) {
                this.usuarioService = usuarioService;
                this.excelExport = excelExport;
                this.usuarioRepository = usuarioRepository;
                this.pdfExportService = pdfExportService;
        }

        @Operation(

                        summary = "Criar um novo usuário",

                        description = "Cria um novo usuário no sistema com os dados fornecidos.",

                        responses = {

                                        @ApiResponse(

                                                        responseCode = "201",

                                                        description = "Usuário criado com sucesso",

                                                        content = @Content(

                                                                        mediaType = "application/json",

                                                                        schema = @Schema(

                                                                                        implementation = UsuarioDTO.class))),
                                        @ApiResponse(

                                                        responseCode = "400",

                                                        description = "Requisição inválida")
                        })
        @PostMapping("/criar")
        public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody @Valid UsuarioDTO usuarioDto) {
                UsuarioDTO body = this.usuarioService.criarUsuario(usuarioDto);
                return new ResponseEntity<UsuarioDTO>(body, HttpStatus.CREATED);
        }

        @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista paginada de todos os usuários cadastrados")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })

        @GetMapping
        public ResponseEntity<UsuarioPageDTO> listarUsuariosPaginados(
                        @Parameter(

                                        description = "O número da página a ser exibida, começando do 0, caso não seja passado um valor no parâmetro.",

                                        example = "0")

                        @RequestParam(defaultValue = "0") int page,

                        @Parameter(

                                        description = "O número de itens por página é definido com valor 10, caso o valor não seja passado no parâmetro.",

                                        example = "10")

                        @RequestParam(defaultValue = "10") int size,

                        @Parameter(

                                        description = "Texto para filtrar usuários por nome ou outros campos. Esse parâmetro não é obrigatório.",

                                        example = "João") @RequestParam(required = false) String search,

                        @Parameter(

                                        description = "Data inicial para o filtro de data. Esse parâmetro não é obrigatório.",

                                        example = "2025-01-01T00:00:00")

                        @RequestParam(required = false) LocalDateTime dataInicial,

                        @Parameter(

                                        description = "Data final para o filtro de data. Esse parâmetro não é obrigatório.",

                                        example = "2025-01-31T23:59:59")

                        @RequestParam(required = false) LocalDateTime dataFinal) {
                UsuarioPageDTO usuarioPageDTO = this.usuarioService.listarUsuariosPaginados(page, size, search,
                                dataInicial, dataFinal);
                return new ResponseEntity<UsuarioPageDTO>(usuarioPageDTO, HttpStatus.OK);
        }

        @Operation(

                        summary = "Buscar usuário por ID",

                        description = "Recupera os detalhes de um usuário específico com base no ID fornecido.")
        @ApiResponses(value = {
                        @ApiResponse(

                                        responseCode = "200",

                                        description = "Usuário encontrado",

                                        content = @Content(

                                                        mediaType = "application/json",

                                                        schema = @Schema(

                                                                        implementation = UsuarioDTO.class))),

                        @ApiResponse(

                                        responseCode = "404",

                                        description = "Usuário não encontrado"),
                        @ApiResponse(

                                        responseCode = "400",

                                        description = "ID do usuário inválido")
        })

        @GetMapping("/{usuarioId}")
        public ResponseEntity<UsuarioDTO> buscarUsuarioPorID(
                        @Parameter(

                                        description = "ID do usuário a ser consultado",

                                        example = "1")

                        @PathVariable @NotNull @Positive Long usuarioId) {
                Usuario usuario = this.usuarioService.buscarUsuarioPorId(usuarioId);
                UsuarioDTO usuarioDto = this.usuarioService.toDto(usuario);
                return new ResponseEntity<UsuarioDTO>(usuarioDto, HttpStatus.OK);
        }

        @Operation(

                        summary = "Atualizar um usuário",

                        description = "Atualiza os dados de um usuário existente com base no ID fornecido.",

                        responses = {

                                        @ApiResponse(

                                                        responseCode = "200",

                                                        description = "Usuário atualizado com sucesso",

                                                        content = @Content(mediaType = "application/json",

                                                                        schema = @Schema(

                                                                                        implementation = UsuarioDTO.class))),

                                        @ApiResponse(

                                                        responseCode = "400",

                                                        description = "Requisição inválida"),

                                        @ApiResponse(

                                                        responseCode = "404",

                                                        description = "Usuário não encontrado")
                        })
        @PutMapping("/{usuarioId}")
        public ResponseEntity<UsuarioDTO> atualizarUsuario(

                        @Parameter(

                                        description = "ID do usuário a ser atualizado",

                                        example = "1")

                        @PathVariable @NotNull @Positive Long usuarioId,

                        @io.swagger.v3.oas.annotations.parameters.RequestBody(

                                        description = "Dados atualizados do usuário",

                                        required = true,

                                        content = @Content(

                                                        mediaType = "application/json",

                                                        schema = @Schema(

                                                                        implementation = UsuarioDTO.class)))

                        @RequestBody UsuarioDTO usuarioDto) {
                UsuarioDTO body = this.usuarioService.atualizarUsuario(usuarioId, usuarioDto);
                return new ResponseEntity<UsuarioDTO>(body, HttpStatus.OK);
        }

        @Operation(

                        summary = "Remover um usuário",

                        description = "Remove um usuário do sistema com base no ID fornecido.",

                        responses = {

                                        @ApiResponse(

                                                        responseCode = "204",

                                                        description = "Usuário removido com sucesso"),

                                        @ApiResponse(

                                                        responseCode = "404",

                                                        description = "Usuário não encontrado"),

                                        @ApiResponse(

                                                        responseCode = "400",

                                                        description = "Requisição inválida")
                        })

        @DeleteMapping("/{usuarioId}")
        public ResponseEntity<Void> removerUsuario(

                        @Parameter(

                                        description = "ID do usuário a ser removido",

                                        required = true,

                                        example = "1")

                        @PathVariable @NotNull @Positive Long usuarioId) {
                this.usuarioService.removerUsuario(usuarioId);
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }

        @GetMapping("/export/xlsx")
        public ResponseEntity<InputStreamResource> exportClientsToExcel() throws IOException {
                List<Usuario> usuarios = this.usuarioRepository.findAll();

                ByteArrayInputStream in = this.excelExport.exportClientsToExcel(usuarios);

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment; filename=clients.xlsx");

                return ResponseEntity
                                .ok()
                                .headers(headers)
                                .contentType(MediaType.parseMediaType(
                                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                                .body(new InputStreamResource(in));
        }

        @GetMapping("/export/pdf")
        public ResponseEntity<byte[]> gerarRelatorioClientes() throws IOException {
                List<Usuario> usuarios = this.usuarioRepository.findAll();

                byte[] pdfBytes = this.pdfExportService.gerarPdf(usuarios);

                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio-clientes.pdf")
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(pdfBytes);
        }
}
