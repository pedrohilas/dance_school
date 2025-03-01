package com.example.escoladanca.repository;

import com.example.escoladanca.model.Curso;
import com.example.escoladanca.model.Professor;

import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    @Transactional
    public void deleteProfessorAndUnlinkCourses(Long professorId) {
        Professor professor = entityManager.find(Professor.class, professorId);
        if (professor != null) {
            List<Curso> cursos = cursoRepository.findAllByProfessorId(professorId);
            for (Curso curso : cursos) {
                curso.setProfessor(null);
                cursoRepository.save(curso);
            }
            entityManager.remove(professor);
        }
    }
}
