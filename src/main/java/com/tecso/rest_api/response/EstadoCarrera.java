package com.tecso.rest_api.response;

import com.tecso.rest_api.entity.InscripcionCarrera;
import com.tecso.rest_api.entity.InscripcionCurso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadoCarrera {

    private InscripcionCarrera inscripcionCarrera;
    private List<InscripcionCurso> inscripcionesCursosActuales;
    private List<InscripcionCurso> inscripcionesCursosAnteriores;
    private Double promedio;
}
