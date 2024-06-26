package com.swprojets.productsales.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swprojets.productsales.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
  
    Optional<Category> findByName(String name);
}
