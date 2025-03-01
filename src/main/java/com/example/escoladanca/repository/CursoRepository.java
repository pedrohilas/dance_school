package com.example.escoladanca.repository;

import com.example.escoladanca.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findAllByProfessorId(Long professorId);
    boolean existsByNome(String nome);

}
