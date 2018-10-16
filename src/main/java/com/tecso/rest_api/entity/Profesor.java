package com.tecso.rest_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profesor extends Persona {

    @Column(nullable = false)
    private Integer legajo;

    @OneToMany(mappedBy = "profesor")
    @JsonIgnore
    private Set<Curso> cursos;
}
