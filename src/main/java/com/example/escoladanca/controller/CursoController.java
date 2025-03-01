package com.example.escoladanca.controller;

import com.example.escoladanca.model.Curso;
import com.example.escoladanca.repository.CursoRepository;
import com.example.escoladanca.repository.ProfessorRepository;
import com.example.escoladanca.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private CursoService cursoService;

    @GetMapping
    public String getAllCursos(Model model) {
        model.addAttribute("cursos", cursoRepository.findAll());
        return "cursos";
    }

    @GetMapping("/novo")
    public String novoCursoForm(Model model) {
        model.addAttribute("curso", new Curso());
        model.addAttribute("professores", professorRepository.findAll());
        return "curso-form";
    }

    @PostMapping
    public String createCurso(@ModelAttribute Curso curso, RedirectAttributes redirectAttributes) {
        if (cursoService.cursoexiste(curso.getNome())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Curso com esse nome já existe!");
        } else {
            // Tratar o caso "Por definir"
            if (curso.getProfessor() != null && curso.getProfessor().getId() == 0) {
                curso.setProfessor(null); // ou outro tratamento conforme sua lógica de negócio
            }
            cursoRepository.save(curso);
            redirectAttributes.addFlashAttribute("successMessage", "Curso criado com sucesso!");
        }
        return "redirect:/cursos";
    }




    @GetMapping("/editar/{id}")
    public String editarCursoForm(@PathVariable Long id, Model model) {
        Curso curso = cursoRepository.findById(id).orElse(null);
        if (curso != null) {
            model.addAttribute("curso", curso);
            model.addAttribute("professores", professorRepository.findAll());
            return "curso-formedit";
        }
        return "redirect:/edit2";
    }

    @PostMapping("/edit2")
    public String updateCurso(@ModelAttribute Curso curso,RedirectAttributes redirectAttributes) {

        cursoRepository.save(curso);
        redirectAttributes.addFlashAttribute("successMessage", "Curso atualizado com sucesso!");

        return "redirect:/cursos";
    }


    @GetMapping("/deletar/{id}")
    public String deleteCursoById(@PathVariable Long id, Model model,RedirectAttributes redirectAttributes) {
        try {
            cursoService.deleteCursoById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Curso deletado com sucesso!");
        } catch (Exception e) {
           redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
            return "redirect:/cursos";
          }

}