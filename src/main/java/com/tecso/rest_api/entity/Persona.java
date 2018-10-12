package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Inheritance(strategy= InheritanceType.JOINED)
@Getter
@Setter
public class Persona extends ResourceSupport {

    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected Integer identificador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected TipoDocumento tipoDocumento;

    @Column(nullable = false)
    protected Integer documento;

    @Column(nullable = false)
    protected String nombre;

    @Column(nullable = false)
    protected String apellido;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fechanac", nullable = false)
    protected LocalDate fechaNacimiento;

}