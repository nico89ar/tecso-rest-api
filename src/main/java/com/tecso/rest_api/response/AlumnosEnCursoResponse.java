package com.tecso.rest_api.response;

import com.tecso.rest_api.entity.Alumno;
import com.tecso.rest_api.entity.Curso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class AlumnosEnCursoResponse extends ResourceSupport {
    private Curso curso;
    private List<Alumno> alumnos;
}