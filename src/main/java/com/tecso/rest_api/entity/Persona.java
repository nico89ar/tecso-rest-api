package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Inheritance(strategy= InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = false)
public class Persona extends ResourceSupport {

    @Id
    @SequenceGenerator(name="persona_identificador_seq",
            sequenceName="persona_identificador_seq",
            allocationSize=50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="persona_identificador_seq")
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
