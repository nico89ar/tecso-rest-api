package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Curso extends ResourceSupport {

    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer identificador;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(name = "cupomaximo", nullable = false)
    private Integer cupoMaximo;

    @Column(nullable = false)
    private Integer anio;

    @OneToOne
    @JoinColumn(name = "idcarrera", nullable = false)
    private Carrera carrera;

    @OneToOne
    @JoinColumn(name = "idprofesor", nullable = false)
    private Profesor profesor;

    @OneToMany(mappedBy = "curso")
    @JsonIgnore
    private Set<InscripcionCurso> inscripcionesCursos;
}
