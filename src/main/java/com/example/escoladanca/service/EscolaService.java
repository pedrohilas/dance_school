package com.example.escoladanca.service;

import com.example.escoladanca.model.Aluno;
import com.example.escoladanca.model.Curso;
import com.example.escoladanca.repository.AlunoRepository;
import com.example.escoladanca.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EscolaService {
    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private AlunoRepository alunoRepository;




    // Implementação dos métodos para obter informações da escola
    public Curso getCursoComMaisAlunos() {
        List<Curso> cursos = cursoRepository.findAll();
        return cursos.stream()
                .max(Comparator.comparingInt(curso -> curso.getAlunos().size()))
                .orElse(null);
    }

    public List<Curso> getCursosEmFuncionamento() {
        return cursoRepository.findAll();
    }

    public Curso getCursoComMaisHoras() {
        List<Curso> cursos = cursoRepository.findAll();
        return cursos.stream()
                .max(Comparator.comparingInt(Curso::getNumeroHoras))
                .orElse(null);
    }

    public Map<String, Double> getIdadeMediaPorCurso() {
        List<Curso> cursos = cursoRepository.findAll();
        return cursos.stream().collect(Collectors.toMap(
                Curso::getNome,
                curso -> curso.getAlunos().stream().mapToInt(Aluno::getIdade).average().orElse(0)
        ));
    }

}
