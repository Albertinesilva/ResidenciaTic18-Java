package br.com.residenciatic18.avaliacao.api.ap002.service;

import org.springframework.core.io.ClassPathResource;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private SpringTemplateEngine templateEngine;

  public void RegistrationConfirmationEmail(String username) throws MessagingException {
    // Alguns servidores de e-mail aceitam caracteres especias que podem acabar
    // calsando problemas na URL por esse motivo é necessário codificar o username
    // para base64.
    String codigo = Base64.getEncoder().encodeToString(username.getBytes());
    sendRegistrationConfirmationRequest(username, codigo);
  }

  public void sendRegistrationConfirmationRequest(String destino, String codigo) throws MessagingException {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
        "UTF-8");

    Context context = new Context();
    context.setVariable("titulo", "Bem-vindo ao ParkApi");
    context.setVariable("texto", "Precisamos que você confirme seu cadastro clicando no link abaixo");
    context.setVariable("linkConfirmacao",
        "http://localhost:8080/api/v1/usuarios/confirmacao/cadastro?codigo=" + codigo);

    String html = templateEngine.process("email/confirmacao", context);
    helper.setTo(destino);
    helper.setText(html, true);
    helper.setSubject("Confirmação de cadastro");
    helper.setFrom("nao-responder@park.com.br");

    helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

    javaMailSender.send(mimeMessage);
  }

  public void sendOrderResetPassword(String destino, String verificador) throws MessagingException {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
        "UTF-8");

    Context context = new Context();
    context.setVariable("titulo", "Redefinição de senha");
    context.setVariable("texto",
        "Para redefinir sua senha, clique no link para lhe direcionar. Token valido por 10 min."
            + " quando exigido no formulário");
    // context.setVariable("verificador", verificador);
    context.setVariable("linkConfirmacao", "http://localhost:8080/api/v1/password-reset?token=" + verificador);

    String html = templateEngine.process("email/confirmacao", context);
    helper.setTo(destino);
    helper.setText(html, true);
    helper.setSubject("Redefinição de senha");
    helper.setFrom("nao-responder@park.com.br");

    helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

    javaMailSender.send(mimeMessage);
  }
}
