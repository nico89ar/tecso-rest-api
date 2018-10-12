package com.tecso.rest_api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Profesor extends Persona {

    @Column(nullable = false)
    private Integer legajo;
}
