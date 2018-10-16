package com.tecso.rest_api.resources;

import com.fasterxml.jackson.annotation.JsonView;
import com.tecso.rest_api.util.LinksHelper;
import com.tecso.rest_api.entity.*;
import com.tecso.rest_api.response.EstadoAcademicoResponse;
import com.tecso.rest_api.response.EstadoCarrera;
import com.tecso.rest_api.service.ServicioAlumnos;
import com.tecso.rest_api.service.ServicioInscripciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/alumnos", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
public class RecursoAlumnos {

    @Autowired
    ServicioAlumnos servicioAlumnos;

    @Autowired
    ServicioInscripciones servicioInscripciones;

    @GetMapping("/{id}")
    public ResponseEntity obtenerAlumno(@PathVariable Integer id) {
        Alumno alumno = servicioAlumnos.obtenerAlumno(id);

        if (alumno == null) {
            return ResponseEntity.notFound().build();
        } else {
            LinksHelper.agregarLinks(alumno);
            Resource<Alumno> respuesta = new Resource<>(alumno);
            return ResponseEntity.ok(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity listarAlumnos() {
        List<Resource<Alumno>> alumnos = servicioAlumnos.listarAlumnos().stream()
                .map(alumno -> {
                    LinksHelper.agregarLinks(alumno);
                    return new Resource<>(alumno);
                })
                .collect(Collectors.toList());

        Resources<Resource<Alumno>> respuesta = new Resources<>(alumnos,
                linkTo(methodOn(RecursoAlumnos.class).listarAlumnos()).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity agregarAlumno(@RequestBody Alumno alumno) {
        Alumno alumnoAgregado = servicioAlumnos.guardarAlumno(alumno);
        LinksHelper.agregarLinks(alumnoAgregado);
        Resource<Alumno> respuesta = new Resource<>(alumnoAgregado);
        return ResponseEntity.created(URI.create(alumnoAgregado.getLink("self").getHref())).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity modificarAlumno(@RequestBody Alumno alumno, @PathVariable Integer id) {
        Alumno alumnoModificado = servicioAlumnos.modificarAlumno(alumno, id);

        if(alumnoModificado == null) {
            return ResponseEntity.notFound().build();
        } else {
            LinksHelper.agregarLinks(alumnoModificado);
            Resource<Alumno> respuesta = new Resource<>(alumnoModificado);
            return ResponseEntity.ok(respuesta);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarAlumno(@PathVariable Integer id) {
        servicioAlumnos.eliminarAlumno(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/estado-academico")
    @JsonView(View.EstadoAcademico.class)
    public ResponseEntity obtenerEstadoAcademico(@PathVariable Integer id) {
        Alumno alumno = servicioAlumnos.obtenerAlumno(id);

        if (alumno == null) {
            return ResponseEntity.notFound().build();
        } else {
            EstadoAcademicoResponse estadoAcademico = construirEstadoAcademico(alumno);
            LinksHelper.agregarLinks(estadoAcademico);
            Resource<EstadoAcademicoResponse> respuesta = new Resource<>(estadoAcademico);
            return ResponseEntity.ok(respuesta);
        }
    }

    private EstadoAcademicoResponse construirEstadoAcademico(Alumno alumno) {
        EstadoAcademicoResponse estadoAcademico = new EstadoAcademicoResponse();
        estadoAcademico.setAlumno(alumno);
        List<EstadoCarrera> estadoCarreras = new ArrayList<>();
        for (InscripcionCarrera inscripcionCarrera : alumno.getInscripcionesCarreras()) {
            estadoCarreras.add(construirEstadoCarrera(inscripcionCarrera));
            LinksHelper.agregarLinks(inscripcionCarrera);
        }
        estadoAcademico.setEstadoCarreras(estadoCarreras);

        return estadoAcademico;
    }

    private EstadoCarrera construirEstadoCarrera(InscripcionCarrera inscripcionCarrera) {
        EstadoCarrera estadoCarrera = new EstadoCarrera();
        estadoCarrera.setInscripcionCarrera(inscripcionCarrera);
        List<InscripcionCurso> cursosActuales = new ArrayList<>();
        List<InscripcionCurso> cursosAnteriores = new ArrayList<>();
        Integer totalAprobadas = 0;
        Double totalNotas = 0.0;
        for (InscripcionCurso inscripcionCurso : servicioInscripciones.listarInsripcionesCursosPorAlumnoYCarrera(inscripcionCarrera.getAlumno(), inscripcionCarrera.getCarrera())) {
            if (inscripcionCurso.getCurso().getAnio() < LocalDate.now().getYear()) {
                cursosAnteriores.add(inscripcionCurso);
            } else {
                cursosActuales.add(inscripcionCurso);
            }
            if(inscripcionCurso.getNota() != null && inscripcionCurso.getNota() >= 6) {
                totalAprobadas++;
                totalNotas += inscripcionCurso.getNota();
            }
            LinksHelper.agregarLinks(inscripcionCurso);
        }
        estadoCarrera.setPromedio(totalNotas / totalAprobadas);
        estadoCarrera.setInscripcionesCursosActuales(cursosActuales);
        estadoCarrera.setInscripcionesCursosAnteriores(cursosAnteriores);
        return estadoCarrera;
    }
}
