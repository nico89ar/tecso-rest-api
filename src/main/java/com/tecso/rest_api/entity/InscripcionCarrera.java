package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inscripciones_carrera")
@IdClass(InscripcionCarreraId.class)
@NoArgsConstructor
@Getter
@Setter
public class InscripcionCarrera {

    @Id
    @ManyToOne
    @JoinColumn(name = "idalumno")
    private Alumno alumno;

    @Id
    @ManyToOne
    @JoinColumn(name = "idcarrera")
    private Carrera carrera;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fechainscripcion", nullable = false)
    private LocalDate fechaInscripcion;
}
