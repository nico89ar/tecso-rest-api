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
public class InscripcionCursoId implements Serializable {

    private Integer curso;
    private Integer alumno;

    @Override
    public int hashCode() {
        return this.curso + this.alumno;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof InscripcionCursoId) {
            InscripcionCursoId otro = (InscripcionCursoId) object;
            return  this.alumno == otro.alumno && this.curso == otro.curso;
        }
        return false;
    }
}
