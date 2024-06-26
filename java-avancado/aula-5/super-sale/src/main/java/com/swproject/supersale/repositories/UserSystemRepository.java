package com.swproject.supersale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swproject.supersale.entities.UserSystem;

@Repository
public interface UserSystemRepository extends JpaRepository<UserSystem, Long>{
  
}
