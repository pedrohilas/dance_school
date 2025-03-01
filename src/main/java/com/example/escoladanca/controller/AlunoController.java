package com.example.escoladanca.controller;

import com.example.escoladanca.model.Aluno;
import com.example.escoladanca.model.AppUser;
import com.example.escoladanca.model.Professor;
import com.example.escoladanca.repository.AlunoRepository;
import com.example.escoladanca.repository.CursoRepository;
import com.example.escoladanca.repository.UserRepository;
import com.example.escoladanca.service.AlunoService;
import com.example.escoladanca.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.util.Optional;

@Controller
@RequestMapping("/alunos")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private UserService userservice;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @GetMapping
    public String listAllAlunos(Model model) {
        model.addAttribute("alunos", alunoService.getAllAlunos());
        return "alunos"; // nome do template Thymeleaf
    }

    @GetMapping("/deletar/{nome}")
    public String deleteAluno(@PathVariable String nome, RedirectAttributes redirectAttributes) {
        Logger logger = LoggerFactory.getLogger(ProfessorController.class);
        try {
            alunoService.deleteAlunoAndUserByName(nome);
            redirectAttributes.addFlashAttribute("successMessage", "Aluno deletado com sucesso!");
        } catch (RuntimeException e) {
            logger.error("Erro ao deletar Aluno: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/alunos";
    }

    @GetMapping("/novo")
    public String novoAlunoForm(Model model) {
        model.addAttribute("aluno", new Aluno());
        model.addAttribute("cursos", cursoRepository.findAll());
        return "aluno-form";
    }


    @GetMapping("/editar/{id}")
    public String editarAlunoForm(@PathVariable Long id, Model model) {
        Aluno aluno = alunoRepository.findById(id).orElse(null);
        if (aluno != null) {
            aluno.setOriginalEmail(aluno.getEmail()); // Preenche o email original
            model.addAttribute("aluno", aluno);
            model.addAttribute("cursos", cursoRepository.findAll());
            return "aluno-formedit";
        }
        return "redirect:/alunos";
    }

    @PostMapping("/edit2")
    public String updateAluno(@ModelAttribute Aluno aluno, RedirectAttributes redirectAttributes) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Obtenha o email original do aluno
        String originalEmail = aluno.getOriginalEmail();

        // Log para verificar o email que está sendo procurado
        System.out.println("Procurando usuário com email original: " + originalEmail);

        // Buscar usuário pelo email original
        AppUser existingUser = userRepository.findByUsername(originalEmail);

        // Verificar se o usuário existe
        if (existingUser == null) {
            // Log de usuário não encontrado
            System.out.println("Usuário não encontrado com email: " + originalEmail);
            redirectAttributes.addFlashAttribute("errorMessage", "Usuário não encontrado.");
            return "redirect:/alunos";
        }

        // Log de usuário encontrado
        System.out.println("Usuário encontrado: " + existingUser.getUsername());

        // Atualizar as informações do usuário existente
        existingUser.setUsername(aluno.getEmail());
        existingUser.setPassword(passwordEncoder.encode(aluno.getPassword())); // Encriptando a senha
        userRepository.save(existingUser);

        // Salvar o aluno
        alunoRepository.save(aluno);

        // Mensagem de sucesso
        redirectAttributes.addFlashAttribute("successMessage", "Aluno atualizado com sucesso!");
        return "redirect:/alunos";
    }

    @PostMapping
    public String createAluno(@ModelAttribute Aluno aluno, RedirectAttributes redirectAttributes, @ModelAttribute AppUser us) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (userservice.userexiste(aluno.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Conta com esse email já existe!");
        } else {
            AppUser user = UserService.convertAlunoToUser(aluno);
            user.setPassword(aluno.getPassword());
            alunoRepository.save(aluno); // Salva o aluno apenas se o nome não existir

            UserService userService = new UserService(userRepository, passwordEncoder);
            userService.saveUser(user, user.getRole()); // Salva o usuário correspondente
            redirectAttributes.addFlashAttribute("successMessage", "Aluno registado com sucesso!");

        }
        return "redirect:/alunos";
    }


}
