package com.persistencia.pratica13.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.persistencia.pratica13.dto.UsuarioDTO;
import com.persistencia.pratica13.entities.Usuario;
import com.persistencia.pratica13.repositories.UsuarioRepository;

@Service
@Transactional(readOnly = false)
public class UsuarioServiceImpl implements UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @SuppressWarnings("null")
  @Override
  public void salvarUsuario(Usuario usuario) {
    usuarioRepository.save(toUsuario());
  }

  @Override
  public void editar(Long id) {
    Usuario usuario = buscarPorId(id);
    usuario.setNome("Paulo Henrique da Silva");
    usuario.setEmail("paulo@gmail.com");
    usuarioRepository.save(usuario);
  }

  @Override
  public void excluir(Long id) {
    usuarioRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Usuario buscarPorId(Long id) {
    return usuarioRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para condutor:" + id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Usuario> buscarTodos() {
    return usuarioRepository.findAll();
  }

  @Override
  public List<UsuarioDTO> listarUsuarios() {

    List<Usuario> usuarios = usuarioRepository.findAll();
    List<UsuarioDTO> usuariosDTO = new ArrayList<>();

    for (Usuario usuario : usuarios) {
      usuariosDTO.add(new UsuarioDTO(usuario));
    }
    return usuariosDTO;

  }

  @Override
  public Usuario toUsuario() {
    Usuario usuario = new Usuario();
    usuario.setNome("Paulo Henrique da Silva");
    usuario.setEmail("paulo@gmail.com");
    usuario.setSenha("12345678");
    return usuario;
  }

  @Override
  public List<UsuarioDTO> buscarPorNome(String nome) {

    List<Usuario> usuarios;
    if (nome == null) {
      usuarios = buscarTodos();
    } else {
      usuarios = usuarioRepository.findByNome(nome);
    }
    List<UsuarioDTO> usuariosDTO = new ArrayList<>();

    for (Usuario usuario : usuarios) {
      usuariosDTO.add(new UsuarioDTO(usuario));
    }

    return usuariosDTO;
  
  }

}