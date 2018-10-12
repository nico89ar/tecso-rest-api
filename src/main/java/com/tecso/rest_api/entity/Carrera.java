package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
public class Carrera extends ResourceSupport {

    @Id
    @GeneratedValue
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
    @JsonIgnore
    private Set<InscripcionCarrera> inscripcionesCarreras;
}
