package br.com.residenciatic18.avaliacao.api.ap002.jwt;

import br.com.residenciatic18.avaliacao.api.ap002.entity.UserSystem;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class JwtUserDetails extends User {

    private UserSystem usuario;

    public JwtUserDetails(UserSystem usuario) {
        super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.usuario = usuario;
    }

    public Long getId() {
        return this.usuario.getId();
    }

    public String getRole() {
        return this.usuario.getRole().name();
    }
}
