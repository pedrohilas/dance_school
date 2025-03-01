package com.example.escoladanca.repository;

import com.example.escoladanca.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByNome(String nome);
    boolean existsByNome(String nome);
    boolean existsByEmail(String email);

}
