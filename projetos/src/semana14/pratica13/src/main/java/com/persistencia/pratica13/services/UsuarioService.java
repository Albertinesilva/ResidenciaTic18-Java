package com.persistencia.pratica13.services;

import java.util.List;

import com.persistencia.pratica13.dto.UsuarioDTO;
import com.persistencia.pratica13.entities.Usuario;

public interface UsuarioService {

  void salvarUsuario(Usuario usuario);

  void editar(Long id);

  void excluir(Long id);

  Usuario buscarPorId(Long id);

  List<Usuario> buscarTodos();

  List<UsuarioDTO> buscarPorNome(String nome);

  List<UsuarioDTO> listarUsuarios();

  Usuario toUsuario();

}