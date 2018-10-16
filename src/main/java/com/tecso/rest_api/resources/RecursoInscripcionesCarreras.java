package com.tecso.rest_api.resources;

import com.tecso.rest_api.entity.InscripcionCarrera;
import com.tecso.rest_api.entity.InscripcionCarreraId;
import com.tecso.rest_api.service.ServicioAlumnos;
import com.tecso.rest_api.service.ServicioCarreras;
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
public class RecursoInscripcionesCarreras {

    @Autowired
    ServicioInscripciones servicioInscripciones;

    @Autowired
    ServicioAlumnos servicioAlumnos;

    @Autowired
    ServicioCarreras servicioCarreras;

    @GetMapping({"/carreras/{idCarrera}/alumnos/{idAlumno}", "/alumnos/{idAlumno}/carreras/{idCarrera}"})
    public ResponseEntity obtenerInscripcionCarrera(@PathVariable Integer idCarrera, @PathVariable Integer idAlumno) {

        InscripcionCarrera inscripcionCarrera = servicioInscripciones.obtenerInscripcionCarrera(new InscripcionCarreraId(idCarrera, idAlumno));

        if (inscripcionCarrera == null) {
            return ResponseEntity.notFound().build();
        } else {
            LinksHelper.agregarLinks(inscripcionCarrera);
            Resource<InscripcionCarrera> respuesta = new Resource<>(inscripcionCarrera);
            return ResponseEntity.ok(respuesta);
        }
    }

    @GetMapping("/inscripciones-carreras")
    public ResponseEntity listarInscripcionesCarreras() {
        List<Resource<InscripcionCarrera>> inscripciones = servicioInscripciones.listarInscripcionesCarreras().stream()
                .map(inscripcionCarrera -> {
                    LinksHelper.agregarLinks(inscripcionCarrera);
                    return new Resource<>(inscripcionCarrera);
                })
                .collect(Collectors.toList());

        Resources<Resource<InscripcionCarrera>> respuesta = new Resources<>(inscripciones,
                linkTo(methodOn(RecursoInscripcionesCarreras.class).listarInscripcionesCarreras()).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping({"/carreras/{idCarrera}/alumnos/{idAlumno}", "/alumnos/{idAlumno}/carreras/{idCarrera}"})
    public ResponseEntity inscribirCarrera(@RequestBody InscripcionCarrera inscripcionCarrera,
                                         @PathVariable Integer idCarrera,
                                         @PathVariable Integer idAlumno) {

        Boolean inscripcionExiste = servicioInscripciones.obtenerInscripcionCarrera(new InscripcionCarreraId(idCarrera, idAlumno)) != null;

        inscripcionCarrera.setAlumno(servicioAlumnos.obtenerAlumno(idAlumno));
        inscripcionCarrera.setCarrera(servicioCarreras.obtenerCarrera(idCarrera));
        if(inscripcionCarrera.getFechaInscripcion() == null) {
            inscripcionCarrera.setFechaInscripcion(LocalDate.now());
        }

        InscripcionCarrera inscripcionGuardada = servicioInscripciones.inscribirCarrera(inscripcionCarrera);

        LinksHelper.agregarLinks(inscripcionGuardada);
        Resource<InscripcionCarrera> respuesta = new Resource<>(inscripcionGuardada);
        if (inscripcionExiste) {
            return ResponseEntity.ok().body(respuesta);
        } else {
            return ResponseEntity.created(URI.create(inscripcionGuardada.getLink("self").getHref())).body(respuesta);
        }
    }
}
