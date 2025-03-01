package com.example.escoladanca.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private int numeroHoras;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToMany(mappedBy = "cursos",  cascade = CascadeType.ALL)  // Certifique-se de usar fetch eager para carregar alunos
    private Set<Aluno> alunos;



    // Construtor sem argumentos
    public Curso() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getNumeroHoras() {
        return numeroHoras;
    }

    public void setNumeroHoras(int numeroHoras) {
        this.numeroHoras = numeroHoras;
    }

    public Set<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(Set<Aluno> alunos) {
        this.alunos = alunos;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    // Método para remover todas as associações com alunos
    public void removeAllAlunos() {
        for (Aluno aluno : new HashSet<>(alunos)) {
            alunos.remove(aluno);
            aluno.getCursos().remove(this);
        }
    }

}
