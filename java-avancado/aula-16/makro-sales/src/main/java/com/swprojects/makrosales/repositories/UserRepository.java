package com.swprojects.makrosales.repositories;

import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.swprojects.makrosales.entities.User;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {

  private final JdbcClient jdbcClient;

  @Transactional
  public Long saveUser(User user) {
    var insertQuery = """
        INSERT INTO tb_users(username, password, email, role)
        VALUES(?, ?, ?, ?)
        """;
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcClient.sql(insertQuery)
        .param(1, user.getUsername())
        .param(2, user.getPassword())
        .param(3, user.getEmail())
        .param(4, user.getRole())
        .update();
    return keyHolder.getKeyAs(Long.class);
  }

  @Transactional(readOnly = true)
  public Optional<User> findByUsername(String username) {
    var findQuery = "SELECT id, username, password, role, email FROM tb_users WHERE username=:username";
    return jdbcClient.sql(findQuery).param("username", username).query(User.class).optional();
  }
}
