package com.example.escoladanca.repository;

import com.example.escoladanca.model.Aluno;
import com.example.escoladanca.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    boolean existsByUsername(String email);

}
