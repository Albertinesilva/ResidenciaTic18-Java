package com.example.crud_user.web.controllers;

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

import com.example.crud_user.entities.User;
import com.example.crud_user.services.UserService;
import com.example.crud_user.web.dto.UserResponseDto;
import com.example.crud_user.web.dto.UserSenhaDto;
import com.example.crud_user.web.dto.form.UserForm;
import com.example.crud_user.web.dto.mapper.UserMapper;

@RestController
@RequestMapping("/users")
public class UsuarioControler {

  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<UserResponseDto> create(@RequestBody UserForm createDto) {
    User user = userService.save(UserMapper.toUser(createDto));
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
    return ResponseEntity.created(uri).body(UserMapper.toDto(user));
  }

  @GetMapping("/{id}")
  public ResponseEntity<List<UserResponseDto>> getById(@PathVariable Long id) {

    if (!userService.findById(id).isEmpty()) {
      return ResponseEntity.ok().body(userService.findById(id));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/")
  public ResponseEntity<List<UserResponseDto>> getAll() {
    List<UserResponseDto> users = UserMapper.toListDto(userService.findAll());
    return ResponseEntity.ok().body(users);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDto> update(@PathVariable @NonNull Long id, @RequestBody UserForm createDto) {
    try {
      return ResponseEntity.ok(UserMapper.toDto(userService.update(id, createDto)));

    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PatchMapping("/password/{id}")
  public ResponseEntity<Void> updatePassword(@PathVariable @NonNull Long id, @RequestBody UserSenhaDto dto) {
    User user = userService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
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
