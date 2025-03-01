package com.example.escoladanca.service;

import com.example.escoladanca.model.Aluno;
import com.example.escoladanca.model.AppUser;
import com.example.escoladanca.repository.AlunoRepository;
import com.example.escoladanca.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private UserRepository userRepository;


    public Aluno saveAluno(Aluno aluno) throws Exception {
        if (alunoRepository.existsByNome(aluno.getNome())) {
            throw new Exception("Conta com esse nome já existe!");
        }
        return alunoRepository.save(aluno);
    }
    @GetMapping
    public List<Aluno> getAllAlunos() {
        return alunoRepository.findAll();
    }
    public boolean alunoExists(String email) {
        return alunoRepository.existsByEmail(email);
    }


    // Adicionando logger
    private static final Logger logger = LoggerFactory.getLogger(AlunoService.class);

    @Transactional
    public void deleteAlunoAndUserByName(String nome) {
        // Deleta o aluno pelo nome
        Optional<Aluno> alunoOptional = alunoRepository.findByNome(nome);
        if (alunoOptional.isPresent()) {
            Aluno aluno = alunoOptional.get();
            alunoRepository.deleteById(aluno.getId());
            // Tentativa de deletar o usuário associado
            String username = aluno.getEmail(); // Supondo que o username seja o email do aluno
            AppUser user = userRepository.findByUsername(username);
            if (user != null) {
                userRepository.deleteById(user.getId());
            } else {
                logger.warn("Usuário não encontrado para o nome: " + nome + " com username: " + username);
            }
        } else {
            throw new RuntimeException("Aluno não encontrado com o nome: " + nome);
        }
    }




}