package com.swproject.supersale.web.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.swproject.supersale.entities.UserSystem;
import com.swproject.supersale.services.UserSystemService;
import com.swproject.supersale.web.dto.UserSystemResponseDto;
import com.swproject.supersale.web.dto.UserSystemSenhaDto;
import com.swproject.supersale.web.dto.form.UserSystemForm;
import com.swproject.supersale.web.dto.mapper.UserSystemMapper;
import com.swproject.supersale.web.exceptions.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Usuarios", description = "Contém todas as operações aos recursos para cadastro, edição e leitura de um usuário.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UserSystemController {

  @Autowired
  private UserSystemService userService;

  @Operation(summary = "Cria um novo usuário", description = "Recurso para criar um novo usuário no sistema.", responses = {
      @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSystemResponseDto.class))),
      @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "422", description = "Recursos não processados por dados de entrada invalidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
  })
  @PostMapping
  public ResponseEntity<UserSystemResponseDto> create(@Valid @RequestBody UserSystemForm createDto) {
    UserSystem user = userService.save(UserSystemMapper.toUser(createDto));
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
    return ResponseEntity.created(uri).body(UserSystemMapper.toDto(user));
  }

  @Operation(summary = "Recuperar um usuário pelo id", description = "Recurso para recuperar um usuário pelo id.", responses = {
      @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSystemResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Recurso não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserSystemResponseDto> getById(@PathVariable @NonNull Long id) {
    UserSystem user = userService.findById(id);
    return ResponseEntity.ok(UserSystemMapper.toDto(user));
  }

  @GetMapping("/")
  public ResponseEntity<List<UserSystemResponseDto>> getAll() {
    List<UserSystemResponseDto> users = UserSystemMapper.toListDto(userService.findAll());
    return ResponseEntity.ok().body(users);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserSystemResponseDto> update(@PathVariable @NonNull Long id, @RequestBody UserSystemForm createDto) {
    try {
      return ResponseEntity.ok(UserSystemMapper.toDto(userService.update(id, createDto)));

    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Atualizar senha", description = "Recurso para atualizar a senha do usuário.", responses = {
      @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
      @ApiResponse(responseCode = "400", description = "Senha não confere.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "404", description = "Recurso não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
  })
  @PatchMapping("/{id}")
  public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserSystemSenhaDto dto) {
    UserSystem user = userService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") @NonNull Long id) {

    if (userService.isExisteId(id)) {

      try {
        userService.delete(id);
        return ResponseEntity.ok().build();

      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
