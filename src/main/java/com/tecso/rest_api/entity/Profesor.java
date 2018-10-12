package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter
@Setter
public class Profesor extends Persona {

    @Column(nullable = false)
    private Integer legajo;

    @OneToMany(mappedBy = "profesor")
    @JsonIgnore
    private Set<Curso> cursos;
}
