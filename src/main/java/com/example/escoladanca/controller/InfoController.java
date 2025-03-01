package com.example.escoladanca.controller;

import com.example.escoladanca.service.EscolaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @Autowired
    private EscolaService escolaService;


    public InfoController(EscolaService escolaService) {
        this.escolaService = escolaService;
    }

    @GetMapping("/info")
    public String getInfo(Model model) {
        model.addAttribute("cursoComMaisAlunos", escolaService.getCursoComMaisAlunos());
        model.addAttribute("cursosEmFuncionamento", escolaService.getCursosEmFuncionamento());
        model.addAttribute("cursoComMaisHoras", escolaService.getCursoComMaisHoras());
        model.addAttribute("idadeMediaPorCurso", escolaService.getIdadeMediaPorCurso());
        return "info";
    }
}
