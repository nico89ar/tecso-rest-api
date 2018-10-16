package com.tecso.rest_api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionCarreraId implements Serializable {

    private Integer carrera;
    private Integer alumno;

    @Override
    public int hashCode() {
        return this.carrera + this.alumno;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof InscripcionCarreraId) {
            InscripcionCarreraId otro = (InscripcionCarreraId) object;
            return  this.alumno == otro.getAlumno() && this.carrera == otro.getCarrera();
        }
        return false;
    }
}