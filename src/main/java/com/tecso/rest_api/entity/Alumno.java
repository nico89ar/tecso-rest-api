package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Alumno extends Persona {

    @Column(nullable = false)
    private Integer legajo;

    @OneToMany(mappedBy = "alumno")
    @JsonIgnore
    private Set<InscripcionCurso> inscripcionesCursos;

    @OneToMany(mappedBy = "alumno")
    @JsonIgnore
    private Set<InscripcionCarrera> inscripcionesCarreras;
}
