package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = false, exclude={"inscripcionesCursos"})
@ToString(exclude={"inscripcionesCursos"})
public class Curso extends ResourceSupport {

    @Id
    @SequenceGenerator(name="curso_identificador_seq",
            sequenceName="curso_identificador_seq",
            allocationSize=50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="curso_identificador_seq")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer identificador;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(name = "cupomaximo", nullable = false)
    private Integer cupoMaximo;

    @Column(nullable = false)
    private Integer anio;

    @ManyToOne
    @JoinColumn(name = "idcarrera", nullable = false)
    @JsonView(View.Ignorar.class)
    private Carrera carrera;

    @ManyToOne
    @JoinColumn(name = "idprofesor", nullable = false)
    private Profesor profesor;

    @OneToMany(mappedBy = "curso")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<InscripcionCurso> inscripcionesCursos;
}
