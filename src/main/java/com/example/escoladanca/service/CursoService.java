package com.example.escoladanca.service;

import com.example.escoladanca.model.Curso;
import com.example.escoladanca.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CursoService {
    public boolean CursoExiste(String nome) {
        return cursoRepository.existsByNome(nome);
    }
    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }

    public Curso findById(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    public Curso save(Curso curso) {
        return cursoRepository.save(curso);
    }

    public void deleteById(Long id) {
        cursoRepository.deleteById(id);
    }

    public boolean cursoexiste(String nome) {
        return cursoRepository.existsByNome(nome);
    }

    @Transactional
    public void deleteCursoById(Long id) {
        Curso curso = cursoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Curso com ID " + id + " não encontrado."));
        curso.removeAllAlunos(); // Remove as associações com alunos
        cursoRepository.save(curso); // Salva as alterações para garantir a remoção das associações
        cursoRepository.deleteById(id); // Deleta o curso
    }

}
