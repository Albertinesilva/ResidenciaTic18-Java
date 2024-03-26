package com.swproject.shopall.web.dto;

import com.swproject.shopall.entities.AbstractEntity;
import com.swproject.shopall.entities.Usuario;
import com.swproject.shopall.entities.enums.PerfilTipo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioResponseDto extends AbstractEntity {

  private Long id;
  private String username;
  private String perfilTipo;

  public UsuarioResponseDto(Usuario user, PerfilTipo tipo) {
    setId(user.getId());
    this.username = user.getUsername();
    setPerfilTipo(tipo);
  }

  public PerfilTipo getPerfilTipo() {
    return PerfilTipo.fromString(perfilTipo);
  }

  public void setPerfilTipo(PerfilTipo perfilTipo) {
    if (perfilTipo != null) {
      this.perfilTipo = perfilTipo.getCodigo();
    }
  }

}
