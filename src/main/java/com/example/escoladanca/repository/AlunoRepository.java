package com.example.escoladanca.repository;

import com.example.escoladanca.model.Aluno;
import com.example.escoladanca.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByNome(String nome);
    boolean existsByNome(String nome);
    boolean existsByEmail(String email);



}
