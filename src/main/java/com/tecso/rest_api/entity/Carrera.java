package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper =  false, exclude={"inscripcionesCarreras", "cursos"})
@ToString(exclude={"inscripcionesCarreras", "cursos"})
public class Carrera extends ResourceSupport {

    @Id
    @SequenceGenerator(name="carrera_identificador_seq",
            sequenceName="carrera_identificador_seq",
            allocationSize=50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="carrera_identificador_seq")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer identificador;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(name = "fechadesde", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaDesde;

    @Column(name = "fechahasta")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaHasta;

    @OneToMany(mappedBy = "carrera")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<InscripcionCarrera> inscripcionesCarreras;

    @OneToMany(mappedBy = "carrera")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<Curso> cursos;
}
