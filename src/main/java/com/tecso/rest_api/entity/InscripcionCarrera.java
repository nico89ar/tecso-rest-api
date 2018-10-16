package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inscripciones_carrera")
@IdClass(InscripcionCarreraId.class)
@Data
@EqualsAndHashCode(callSuper = false)
public class InscripcionCarrera extends ResourceSupport {

    @Id
    @ManyToOne
    @JoinColumn(name = "idalumno")
    @JsonView(View.Ignorar.class)
    private Alumno alumno;

    @Id
    @ManyToOne
    @JoinColumn(name = "idcarrera")
    private Carrera carrera;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fechainscripcion", nullable = false)
    private LocalDate fechaInscripcion;
}
