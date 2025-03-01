package com.example.escoladanca.controller;

import com.example.escoladanca.model.Aluno;
import com.example.escoladanca.model.AppUser;
import com.example.escoladanca.model.Professor;
import com.example.escoladanca.repository.ProfessorRepository;
import com.example.escoladanca.repository.UserRepository;
import com.example.escoladanca.service.ProfessorService;
import com.example.escoladanca.service.UserService;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private UserService userservice;

    @GetMapping
    public String getAllProfessores(Model model) {
        model.addAttribute("professores", professorRepository.findAll());
        return "professores";
    }

    @GetMapping("/novo")
    public String novoProfessorForm(Model model) {
        model.addAttribute("professor", new Professor());
        return "professor-form";
    }


    @PostMapping
    public String createProfessor(@ModelAttribute AppUser user, RedirectAttributes redirectAttributes,@ModelAttribute Professor professor) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (userservice.userexiste(professor.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Conta com esse email já existe!");
        }else{
        AppUser professor_user = UserService.convertProfessorToUser(professor);
        professor_user.setPassword(professor.getPassword());
        professorRepository.save(professor);

        UserService userService = new UserService(userRepository,passwordEncoder);
        userService.saveUser(professor_user, professor_user.getRole());
            redirectAttributes.addFlashAttribute("successMessage", "Professor registado com sucesso!");
        }
        return "redirect:/professores";
    }

    @GetMapping("/editar/{id}/{email}")
    public String editarProfessorForm(@PathVariable Long id, Model model) {
        Professor professor = professorRepository.findById(id).orElse(null);
        if (professor != null) {
            model.addAttribute("professor", professor);

            return "professor-form";
        }
        return "redirect:/professores";
    }

    @GetMapping("/editar/{id}")
    public String updateProfessor(@PathVariable Long id, Model model) {
        Professor professor = professorRepository.findById(id).orElse(null);
        if (professor != null) {
            professor.setOriginalEmail(professor.getEmail()); // Preenche o email original
            model.addAttribute("professor", professor);
            return "professor-formedit";
        }
        return "redirect:/professores";
    }

    @PostMapping("/edit2")
    public String crea(@ModelAttribute Professor professor, RedirectAttributes redirectAttributes) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Obtenha o email original do professor
        String originalEmail = professor.getOriginalEmail();

        // Log para verificar o email que está sendo procurado
        System.out.println("Procurando usuário com email original: " + originalEmail);

        // Buscar usuário pelo email original
        AppUser existingUser = userRepository.findByUsername(originalEmail); // Supondo que o email é único para usuários

            // Verificar se o usuário existe
            if (existingUser == null) {
                // Log de usuário não encontrado
                System.out.println("Usuário não encontrado com email: " + originalEmail);
                redirectAttributes.addFlashAttribute("errorMessage", "Usuário não encontrado.");
                return "redirect:/professores";
            }
            // Log de usuário encontrado
            System.out.println("Usuário encontrado: " + existingUser.getUsername());
            // Atualizar as informações do usuário existente
            existingUser.setUsername(professor.getEmail()); // Atualize o email do usuário com o novo email
            existingUser.setPassword(passwordEncoder.encode(professor.getPassword())); // Encriptando a senha
            userRepository.save(existingUser);
            // Salvar o professor
            professorRepository.save(professor);

            // Mensagem de sucesso
            redirectAttributes.addFlashAttribute("successMessage", "Professor atualizado com sucesso!");

        return "redirect:/professores";
    }




    @GetMapping("/deletar/{nome}")
    public String deleteProfessor(@PathVariable String nome, RedirectAttributes redirectAttributes) {
        Logger logger = LoggerFactory.getLogger(ProfessorController.class);
        try {
            professorService.deleteProfessorAndUserByName(nome);
            redirectAttributes.addFlashAttribute("successMessage", "Professor deletado com sucesso!");
            logger.info("Professor deletado com sucesso: {}", nome);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Professor associado a um curso");
        }

        return "redirect:/professores";
    }
}

