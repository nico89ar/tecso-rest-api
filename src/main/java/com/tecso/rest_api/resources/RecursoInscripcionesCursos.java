package com.tecso.rest_api.resources;

import com.tecso.rest_api.entity.*;
import com.tecso.rest_api.exceptions.NoInscriptoEnCarreraException;
import com.tecso.rest_api.service.ServicioAlumnos;
import com.tecso.rest_api.service.ServicioCursos;
import com.tecso.rest_api.service.ServicioInscripciones;
import com.tecso.rest_api.util.LinksHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
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
            LinksHelper.agregarLinks(inscripcionCurso);
            Resource<InscripcionCurso> respuesta = new Resource<>(inscripcionCurso);
            return ResponseEntity.ok(respuesta);
        }
    }

    @GetMapping("/inscripciones-cursos")
    public ResponseEntity listarInscripcionesCursos() {
        List<Resource<InscripcionCurso>> inscripciones = servicioInscripciones.listarInscripcionesCursos().stream()
                .map(inscripcionCurso -> {
                    LinksHelper.agregarLinks(inscripcionCurso);
                    return new Resource<>(inscripcionCurso);
                })
                .collect(Collectors.toList());

        Resources<Resource<InscripcionCurso>> respuesta = new Resources<>(inscripciones,
                linkTo(methodOn(RecursoInscripcionesCursos.class).listarInscripcionesCursos()).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping({"/cursos/{idCurso}/alumnos/{idAlumno}", "/alumnos/{idAlumno}/cursos/{idCurso}"})
    public ResponseEntity inscribirCurso(@RequestBody InscripcionCurso inscripcionCurso,
                                         @PathVariable Integer idCurso,
                                         @PathVariable Integer idAlumno) {

        Boolean inscripcionExiste = servicioInscripciones.obtenerInscripcionCurso(new InscripcionCursoId(idCurso, idAlumno)) != null;

        inscripcionCurso.setAlumno(servicioAlumnos.obtenerAlumno(idAlumno));
        inscripcionCurso.setCurso(servicioCursos.obtenerCurso(idCurso));
        if(inscripcionCurso.getFechaInscripcion() == null) {
            inscripcionCurso.setFechaInscripcion(LocalDate.now());
        }

        try {
            InscripcionCurso inscripcionGuardada = servicioInscripciones.inscribirCurso(inscripcionCurso);
            LinksHelper.agregarLinks(inscripcionGuardada);
            Resource<InscripcionCurso> respuesta = new Resource<>(inscripcionGuardada);
            if (inscripcionExiste) {
                return ResponseEntity.ok().body(respuesta);
            } else {
                return ResponseEntity.created(URI.create(inscripcionGuardada.getLink("self").getHref())).body(respuesta);
            }

        } catch (NoInscriptoEnCarreraException e) {
            //TODO agregar error handling y respuesta adecuada
            return ResponseEntity.badRequest().build();
        }
    }
}
