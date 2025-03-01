package com.example.escoladanca.config;

import com.example.escoladanca.converter.StringToProfessorConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private StringToProfessorConverter stringToProfessorConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToProfessorConverter);
    }
}
