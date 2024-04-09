package com.swproject.salescompany.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.github.javafaker.Faker;
import com.swproject.salescompany.entities.Category;
import com.swproject.salescompany.entities.Product;
import com.swproject.salescompany.repositories.ProductRepository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private Faker faker;

    @AfterEach
    public void setup() {
        // faker = new Faker();
    }

    @TestConfiguration
    static class FakerTestConfig {

        @Bean
        public Faker faker() {
            return new Faker();
        }
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(testEntityManager).isNotNull();
        assertThat(productRepository).isNotNull();
    }

    private Product generateFakeProduct() {
        Product product = new Product();
        product.setName(faker.commerce().productName());
        product.setDescription(faker.commerce().material());
        product.setPrice(faker.number().randomDouble(2, 1, 1000));
        product.setImgUrl(faker.internet().url());
        return product;
    }

    @Test
    void createProduct_WithValidData_ReturnsProduct() {
        Product product = generateFakeProduct();

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(savedProduct.getImgUrl()).isEqualTo(product.getImgUrl());

    }

    @Test
    void createProduct_WithExistingNome_ThrowsException() {
        // Crie e persista uma categoria com um nome existente
        Product product1 = generateFakeProduct();
        testEntityManager.persistFlushFind(product1); // Força a persistência imediata

        // Tente criar outra categoria com o mesmo nome
        Product product2 = generateFakeProduct();
        product2.setName(product1.getName());

        // Verifique se uma exceção é lançada ao tentar salvar a segunda categoria
        assertThatThrownBy(() -> productRepository.save(product2)).isInstanceOf(Exception.class);
    }

}
