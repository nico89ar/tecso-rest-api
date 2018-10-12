package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inscripciones_curso")
@IdClass(InscripcionCursoId.class)
@NoArgsConstructor
@Getter
@Setter
public class InscripcionCurso extends ResourceSupport {

    @Id
    @ManyToOne
    @JoinColumn(name = "idalumno")
    private Alumno alumno;

    @Id
    @ManyToOne
    @JoinColumn(name = "idcurso")
    private Curso curso;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fechainscripcion", nullable = false)
    private LocalDate fechaInscripcion;

    private Integer nota;

}
