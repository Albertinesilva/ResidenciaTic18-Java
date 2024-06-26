package br.com.residenciatic18.avaliacao.api.ap002;

import br.com.residenciatic18.avaliacao.api.ap002.web.dto.UserSystemCreateDto;
import br.com.residenciatic18.avaliacao.api.ap002.web.dto.UserSystemResponseDto;
import br.com.residenciatic18.avaliacao.api.ap002.web.dto.UserSystemSenhaDto;
import br.com.residenciatic18.avaliacao.api.ap002.web.exceptions.ErrorMessage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

        @Autowired
        WebTestClient testClient;

        @Test
        public void createUsuario_ComUsernameEPasswordValidos_RetornarUsuarioCriadoComStatus201() {
                UserSystemResponseDto responseBody = testClient
                        .post()
                        .uri("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemCreateDto("joao@Gmail.com", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody(UserSystemResponseDto.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("joao@Gmail.com");
                org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
        }

        @Test
        public void _createUsuario_ComUsernameInvalido_RetornarErrorMessageStatus422() {
                ErrorMessage responseBody = testClient
                        .post()
                        .uri("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemCreateDto("", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                        .post()
                        .uri("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemCreateDto("tody@", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                        .post()
                        .uri("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemCreateDto("tody@email", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        }

        @Test
        public void _createUsuario_ComPasswordInvalido_RetornarErrorMessageStatus422() {
                ErrorMessage responseBody = testClient
                        .post()
                        .uri("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemCreateDto("tody@gmail.com", ""))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                        .post()
                        .uri("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemCreateDto("tody@gmail.com", "123"))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                        .post()
                        .uri("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemCreateDto("tody@gmail.com", "JAVA!@#ResTIc18fg444"))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        }

        @Test
        public void _createUsuario_ComUsernameRepetido_RetornarErrorMessageComStaus409() {
                ErrorMessage responseBody = testClient
                        .post()
                        .uri("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemCreateDto("ana@email.com", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isEqualTo(409)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
        }

        @Test
        public void buscarUsuario_ComIdExistente_RetornarUsuarioComStatus200() {
                UserSystemResponseDto responseBody = testClient
                        .get()
                        .uri("/api/v1/usuarios/100")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "20191tadssaj0026@ifba.edu.br", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(UserSystemResponseDto.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(100);
                org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("20191tadssaj0026@ifba.edu.br");
                org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

                responseBody = testClient
                        .get()
                        .uri("/api/v1/usuarios/101")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "20191tadssaj0026@ifba.edu.br", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(UserSystemResponseDto.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
                org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");
                org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

                responseBody = testClient
                        .get()
                        .uri("/api/v1/usuarios/101")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(UserSystemResponseDto.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
                org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");
                org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
        }

        @Test
        public void buscarUsuario_ComIdInexistente_RetornarErrorMessageComStatus404() {
                ErrorMessage responseBody = testClient
                        .get()
                        .uri("/api/v1/usuarios/0")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "20191tadssaj0026@ifba.edu.br", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isNotFound()
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
        }

        @Test
        public void buscarUsuario_ComUsuarioClienteBuscandoOutroCliente_RetornarErrorMessageComStatus403() {
                ErrorMessage responseBody = testClient
                        .get()
                        .uri("/api/v1/usuarios/102")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isForbidden()
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
        }

        @Test
        public void editarSenha_ComDadosValidos_RetornarStatus204() {
                testClient
                        .patch()
                        .uri("/api/v1/usuarios/100")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "20191tadssaj0026@ifba.edu.br", "JAVA!@#ResTIc18"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemSenhaDto("JAVA!@#ResTIc18", "JAVA!@#ResTIc18", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isNoContent();

                testClient
                        .patch()
                        .uri("/api/v1/usuarios/101")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "JAVA!@#ResTIc18"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemSenhaDto("JAVA!@#ResTIc18", "JAVA!@#ResTIc18", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isNoContent();
        }

        @Test
        public void editarSenha_ComUsuariosDiferentes_RetornarErrorMessageComStatus403() {
                ErrorMessage responseBody = testClient
                        .patch()
                        .uri("/api/v1/usuarios/0")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "20191tadssaj0026@ifba.edu.br", "JAVA!@#ResTIc18"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemSenhaDto("JAVA!@#ResTIc18", "JAVA!@#ResTIc18", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isForbidden()
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

                responseBody = testClient
                        .patch()
                        .uri("/api/v1/usuarios/0")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "JAVA!@#ResTIc18"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemSenhaDto("JAVA!@#ResTIc18", "JAVA!@#ResTIc18", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isForbidden()
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
        }

        @Test
        public void editarSenha_ComCamposInvalidos_RetornarErrorMessageComStatus422() {
                ErrorMessage responseBody = testClient
                        .patch()
                        .uri("/api/v1/usuarios/100")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "20191tadssaj0026@ifba.edu.br", "JAVA!@#ResTIc18"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemSenhaDto("", "", ""))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                        .patch()
                        .uri("/api/v1/usuarios/100")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "20191tadssaj0026@ifba.edu.br", "JAVA!@#ResTIc18"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemSenhaDto("JAVA!@#Res", "JAVA!@#Res", "JAVA!@#Res"))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                        .patch()
                        .uri("/api/v1/usuarios/100")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "20191tadssaj0026@ifba.edu.br", "JAVA!@#ResTIc18"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemSenhaDto("JAVA!@#ResTIc18456", "JAVA!@#ResTIc18456", "JAVA!@#ResTIc18456"))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        }

        @Test
        public void editarSenha_ComSenhaInvalidas_RetornarErrorMessageComStatus400() {
                ErrorMessage responseBody = testClient
                        .patch()
                        .uri("/api/v1/usuarios/100")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "20191tadssaj0026@ifba.edu.br", "JAVA!@#ResTIc18"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemSenhaDto("JAVA!@#ResTIc18", "JAVA!@#ResTIc18", "JAVA!@#ResTIc1"))
                        .exchange()
                        .expectStatus().isEqualTo(400)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

                responseBody = testClient
                        .patch()
                        .uri("/api/v1/usuarios/100")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "20191tadssaj0026@ifba.edu.br", "JAVA!@#ResTIc18"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserSystemSenhaDto("@#ResTIc18", "JAVA!@#ResTIc18", "JAVA!@#ResTIc18"))
                        .exchange()
                        .expectStatus().isEqualTo(400)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
        }

        @Test
        public void listarUsuarios_ComUsuarioComPermissao_RetornarListaDeUsuariosComStatus200() {
            List<UserSystemResponseDto> responseBody = testClient
                    .get()
                    .uri("/api/v1/usuarios")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "20191tadssaj0026@ifba.edu.br", "JAVA!@#ResTIc18"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(UserSystemResponseDto.class)
                    .returnResult().getResponseBody();
    
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(3);
        }
    
        @Test
        public void listarUsuarios_ComUsuarioSemPermissao_RetornarErrorMessageComStatus403() {
            ErrorMessage responseBody = testClient
                    .get()
                    .uri("/api/v1/usuarios")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "JAVA!@#ResTIc18"))
                    .exchange()
                    .expectStatus().isForbidden()
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();
    
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
        }
}
