package com.projetoJAVA.Application.colaboratesEntity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
@Table (name = "colaboratestable")
public class Colaborates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", length = 200, nullable = false)
    private String nome;

    @Column(name = "senha", columnDefinition = "TEXT", nullable = false)
    private String senha;

    @Column(name = "score", columnDefinition = "NUMERIC", nullable = false)
    private String score;

    @ManyToOne
    @JoinColumn(name = "chefe")
    private Colaborates chefe;

    public Colaborates getChefe() {
        return chefe;
    }

    public void setChefe(Colaborates chefs_table) {
        this.chefe = chefs_table;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }
}