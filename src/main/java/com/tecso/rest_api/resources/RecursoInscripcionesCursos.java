package com.tecso.rest_api.resources;

import com.tecso.rest_api.entity.*;
import com.tecso.rest_api.exceptions.NoInscriptoEnCarreraException;
import com.tecso.rest_api.service.ServicioAlumnos;
import com.tecso.rest_api.service.ServicioCursos;
import com.tecso.rest_api.service.ServicioInscripciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
public class RecursoInscripcionesCursos {

    @Autowired
    ServicioInscripciones servicioInscripciones;

    @Autowired
    ServicioAlumnos servicioAlumnos;

    @Autowired
    ServicioCursos servicioCursos;

    @GetMapping({"/cursos/{idCurso}/alumnos/{idAlumno}", "/alumnos/{idAlumno}/cursos/{idCurso}"})
    public ResponseEntity obtenerInscripcionCurso(@PathVariable Integer idCurso, @PathVariable Integer idAlumno) {

        InscripcionCurso inscripcionCurso = servicioInscripciones.obtenerInscripcionCurso(new InscripcionCursoId(idCurso, idAlumno));

        if (inscripcionCurso == null) {
            return ResponseEntity.notFound().build();
        } else {
            Resource<InscripcionCurso> respuesta = new Resource<>(inscripcionCurso, generarLinks(inscripcionCurso));
            return ResponseEntity.ok(respuesta);
        }
    }

    @GetMapping("/inscripciones-cursos")
    public ResponseEntity listarInscripcionesCursos() {
        List<Resource<InscripcionCurso>> inscripciones = servicioInscripciones.listarInscripcionesCursos().stream()
                .map(inscripcionCurso -> new Resource<>(inscripcionCurso, generarLinks(inscripcionCurso)))
                .collect(Collectors.toList());

        Resources<Resource<InscripcionCurso>> respuesta = new Resources<>(inscripciones,
                linkTo(methodOn(RecursoInscripcionesCursos.class).listarInscripcionesCursos()).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping({"/cursos/{idCurso}/alumnos/{idAlumno}", "/alumnos/{idAlumno}/cursos/{idCurso}"})
    public ResponseEntity inscribirCurso(@RequestBody InscripcionCurso inscripcionCurso,
                                         @PathVariable Integer idCurso,
                                         @PathVariable Integer idAlumno) {

        inscripcionCurso.setAlumno(servicioAlumnos.obtenerAlumno(idAlumno));
        inscripcionCurso.setCurso(servicioCursos.obtenerCurso(idCurso));
        if(inscripcionCurso.getFechaInscripcion() == null) {
            inscripcionCurso.setFechaInscripcion(LocalDate.now());
        }

        try {
            InscripcionCurso inscripcionGuardada = servicioInscripciones.inscribirCurso(inscripcionCurso);
            Link linkPropio = linkTo(methodOn(RecursoInscripcionesCursos.class).obtenerInscripcionCurso(idCurso, idAlumno)).withSelfRel();
            Resource<InscripcionCurso> respuesta = new Resource<>(inscripcionGuardada, generarLinks(inscripcionGuardada));
            return ResponseEntity.created(URI.create(linkPropio.getHref())).body(respuesta);
        } catch (NoInscriptoEnCarreraException e) {
            return ResponseEntity.badRequest().body("Primero debe inscribirse a la carrera " + inscripcionCurso.getCurso().getCarrera().getNombre());
        }
    }

    private List<Link> generarLinks(InscripcionCurso inscripcionCurso) {
        ArrayList<Link> links = new ArrayList<>();
        if (!inscripcionCurso.getAlumno().hasLinks()) {
            inscripcionCurso.getAlumno().add(
                    linkTo(methodOn(RecursoAlumnos.class).obtenerAlumno(inscripcionCurso.getAlumno().getIdentificador())).withSelfRel(),
                    linkTo(methodOn(RecursoAlumnos.class).listarAlumnos()).withRel("alumnos"));
        }
        if (!inscripcionCurso.getCurso().hasLinks()) {
            inscripcionCurso.getCurso().add(
                    linkTo(methodOn(RecursoCursos.class).obtenerCurso(inscripcionCurso.getCurso().getIdentificador())).withSelfRel(),
                    linkTo(methodOn(RecursoCursos.class).listarCursos()).withRel("cursos"));
        }
        links.add(linkTo(methodOn(RecursoInscripcionesCursos.class).obtenerInscripcionCurso(
                inscripcionCurso.getCurso().getIdentificador(),
                inscripcionCurso.getAlumno().getIdentificador()))
                .withSelfRel());
        links.add(linkTo(methodOn(RecursoInscripcionesCursos.class).listarInscripcionesCursos()).withRel("inscripciones-cursos"));

        return links;
    }

}
