package com.example.escoladanca.service;

import com.example.escoladanca.model.Professor;
import com.example.escoladanca.model.AppUser;
import com.example.escoladanca.repository.ProfessorRepository;
import com.example.escoladanca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class ProfessorService {

    private static final Logger logger = LoggerFactory.getLogger(ProfessorService.class);

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UserRepository userRepository;


public boolean profexiste(String email) {
    return professorRepository.existsByEmail(email);
}
    @Transactional
    public void deleteProfessorAndUserByName(String nome) {
        // Deleta o professor pelo nome
        Optional<Professor> professorOptional = professorRepository.findByNome(nome);
        if (professorOptional.isPresent()) {
            Professor professor = professorOptional.get();
            professorRepository.deleteById(professor.getId());
            // Tentativa de deletar o usuário associado
            String username = professor.getEmail(); // Supondo que o username seja o email do professor
            AppUser user = userRepository.findByUsername(username);
            if (user != null) {
                userRepository.deleteById(user.getId());
            } else {
                logger.warn("Usuário não encontrado para o nome: " + nome + " com username: " + username);
            }
        } else {
            throw new RuntimeException("Professor não encontrado com o nome: " + nome);
        }
    }
}
