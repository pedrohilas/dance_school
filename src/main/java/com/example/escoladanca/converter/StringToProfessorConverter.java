package com.example.escoladanca.converter;

import com.example.escoladanca.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.example.escoladanca.model.Professor;

@Component
public class StringToProfessorConverter implements Converter<String, Professor> {

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public Professor convert(String source) {
        Long id = Long.valueOf(source);
        return professorRepository.findById(id).orElse(null);
    }
}
