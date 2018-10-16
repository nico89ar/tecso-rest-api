package com.tecso.rest_api.util;

import com.tecso.rest_api.entity.*;
import com.tecso.rest_api.resources.*;
import com.tecso.rest_api.response.AlumnosEnCursoResponse;
import com.tecso.rest_api.response.EstadoAcademicoResponse;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class LinksHelper {

    public static void agregarLinks(Alumno alumno) {
        if (!alumno.hasLinks()) {
            alumno.add(linkTo(methodOn(RecursoAlumnos.class).obtenerAlumno(alumno.getIdentificador())).withSelfRel());
            alumno.add(linkTo(methodOn(RecursoAlumnos.class).listarAlumnos()).withRel("alumnos"));
        }
    }

    public static void agregarLinks(InscripcionCarrera inscripcionCarrera) {
        agregarLinks(inscripcionCarrera.getCarrera());
        agregarLinks(inscripcionCarrera.getAlumno());
        if (!inscripcionCarrera.hasLinks()) {
            inscripcionCarrera.add(linkTo(methodOn(RecursoInscripcionesCarreras.class).obtenerInscripcionCarrera(
                    inscripcionCarrera.getCarrera().getIdentificador(), inscripcionCarrera.getAlumno().getIdentificador())).withSelfRel());
            inscripcionCarrera.add(linkTo(methodOn(RecursoInscripcionesCarreras.class).listarInscripcionesCarreras()).withRel("inscripciones-carreras"));
        }
    }

    public static void agregarLinks(InscripcionCurso inscripcionCurso) {
        agregarLinks(inscripcionCurso.getCurso());
        agregarLinks(inscripcionCurso.getAlumno());
        if (!inscripcionCurso.hasLinks()) {
            inscripcionCurso.add(linkTo(methodOn(RecursoInscripcionesCursos.class).obtenerInscripcionCurso(
                    inscripcionCurso.getCurso().getIdentificador(), inscripcionCurso.getAlumno().getIdentificador())).withSelfRel());
            inscripcionCurso.add(linkTo(methodOn(RecursoInscripcionesCursos.class).listarInscripcionesCursos()).withRel("inscripciones-cursos"));
        }
    }

    public static void agregarLinks(Carrera carrera) {
        if (!carrera.hasLinks()) {
            carrera.add(linkTo(methodOn(RecursoCarreras.class).obtenerCarrera(carrera.getIdentificador())).withSelfRel());
            carrera.add(linkTo(methodOn(RecursoCarreras.class).listarCarreras()).withRel("carreras"));
        }
    }

    public static void agregarLinks(Curso curso) {
        agregarLinks(curso.getProfesor());
        agregarLinks(curso.getCarrera());
        if (!curso.hasLinks()) {
            curso.add(linkTo(methodOn(RecursoCursos.class).obtenerCurso(curso.getIdentificador())).withSelfRel());
            curso.add(linkTo(methodOn(RecursoCursos.class).listarCursos()).withRel("cursos"));
        }
    }

    public static void agregarLinks(Profesor profesor) {
        if (!profesor.hasLinks()) {
            profesor.add(linkTo(methodOn(RecursoProfesores.class).obtenerProfesor(profesor.getIdentificador())).withSelfRel());
            profesor.add(linkTo(methodOn(RecursoProfesores.class).listarProfesores()).withRel("profesores"));
        }
    }

    public static void agregarLinks(EstadoAcademicoResponse estadoAcademicoResponse) {
        agregarLinks(estadoAcademicoResponse.getAlumno());
        if (!estadoAcademicoResponse.hasLinks()) {
            estadoAcademicoResponse.add(linkTo(methodOn(RecursoAlumnos.class).obtenerEstadoAcademico(estadoAcademicoResponse.getAlumno().getIdentificador())).withSelfRel());
        }
    }

    public static void agregarLinks(AlumnosEnCursoResponse alumnosEnCursoResponse) {
        agregarLinks(alumnosEnCursoResponse.getCurso());
        alumnosEnCursoResponse.getAlumnos().forEach(LinksHelper::agregarLinks);
        if (!alumnosEnCursoResponse.hasLinks()) {
            alumnosEnCursoResponse.add(linkTo(methodOn(RecursoCursos.class).listarAlumnosInscriptos(alumnosEnCursoResponse.getCurso().getIdentificador())).withSelfRel());
        }
    }
}
