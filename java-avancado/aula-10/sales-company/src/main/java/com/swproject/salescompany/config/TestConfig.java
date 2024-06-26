package com.swproject.salescompany.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.javafaker.Faker;
import com.swproject.salescompany.entities.Category;
import com.swproject.salescompany.entities.Employee;
import com.swproject.salescompany.entities.Product;

import com.swproject.salescompany.entities.UserSystem;
import com.swproject.salescompany.entities.UserSystem.Role;
import com.swproject.salescompany.repositories.CategoryRepository;
import com.swproject.salescompany.repositories.EmployeeRepository;
import com.swproject.salescompany.repositories.ProductRepository;
import com.swproject.salescompany.repositories.UserSystemRepository;

import br.com.caelum.stella.tinytype.CPF;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

  public static final Logger log = LoggerFactory.getLogger(TestConfig.class);

  @Autowired
  private UserSystemRepository userRepository;

  @Autowired
  private CategoryRepository categoryRepositoy;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Override
  public void run(String... args) throws Exception {

    Faker faker = new Faker(new Locale("pt-BR"));

    for (int i = 0; i < 5; i++) {

      // Gerando uma data de nascimento aleatória
      Instant randomBirthDate = faker.date().birthday().toInstant().truncatedTo(ChronoUnit.SECONDS);

      // Criando uma nova categoria com os dados aleatórios
      Category category = new Category();
      category.setName(faker.commerce().department());
      categoryRepositoy.save(category);
      log.info("Category: {}", category);

      // Criando um novo produto aleatório com a biblioteca Faker e salvando o produto
      // no banco de dados
      Product product = new Product();
      product.setName(faker.commerce().productName());
      product.setDescription(faker.commerce().material());
      product.setPrice(faker.number().randomDouble(2, 10, 1000));
      product.setImgUrl(faker.internet().image());

      // Atribuindo a categoria ao produto e salvando o produto
      product.getCategories().add(category);
      productRepository.save(product);
      log.info("Product: {}", product);

      // Se não houver um administrador, cria um novo
      UserSystem usuario = new UserSystem();

      // Verifica se já existe pelo menos um administrador no banco de dados
      boolean isAdminExists = userRepository.existsByRole(Role.ROLE_ADMIN);

      if (!isAdminExists) {
        usuario.setUsername(faker.internet().emailAddress());
        usuario.setPassword(faker.number().digits(6));
        usuario.setRole(Role.ROLE_ADMIN);
        userRepository.save(usuario);
        log.info("Usuario ADMIN criado: {}", usuario);

      } else {
        // Se já existir um administrador, cria um funcionário
        usuario.setUsername(faker.internet().emailAddress());
        usuario.setPassword(faker.number().digits(6));
        usuario.setRole(Role.ROLE_COMUM);
        userRepository.save(usuario);
        log.info("Usuario FUNCIONARIO criado: {}", usuario);
      }

      // Gerar um número de CPF aleatório
      String cpfString = faker.number().digits(11);

      // Obter o CPF formatado com pontos e traço
      String cpfFormatado = new CPF(cpfString).getNumeroFormatado();

      // Criando um novo employee aleatório com a biblioteca Faker
      Employee employee = new Employee();
      employee.setName(faker.name().fullName());
      employee.setCpf(cpfFormatado);
      employee.setBirthDate(randomBirthDate);
      employee.setIsActive(faker.bool().bool());
      employee.setStartDate(faker.date().birthday());
      employee.setExperienceYears(faker.number().numberBetween(1, 10));

      // Atribuindo o usuário ao funcionário e salvando o funcionário no banco de
      // dados
      employee.getProductsSold().add(product);
      employee.setUsuario(usuario);
      employeeRepository.save(employee);
      log.info("Employee: {}", employee);

    }
  }
}