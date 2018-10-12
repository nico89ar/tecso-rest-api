package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Alumno extends Persona {

    @Column(nullable = false)
    private Integer legajo;

    @OneToMany(mappedBy = "alumno")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<InscripcionCurso> inscripcionesCursos;

    @OneToMany(mappedBy = "alumno")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<InscripcionCarrera> inscripcionesCarreras;
}
