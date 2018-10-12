package com.tecso.rest_api.resources;

import com.tecso.rest_api.entity.InscripcionCarrera;
import com.tecso.rest_api.entity.InscripcionCarreraId;
import com.tecso.rest_api.service.ServicioAlumnos;
import com.tecso.rest_api.service.ServicioCarreras;
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
            Resource<InscripcionCarrera> respuesta = new Resource<>(inscripcionCarrera, generarLinks(inscripcionCarrera));
            return ResponseEntity.ok(respuesta);
        }
    }

    @GetMapping("/inscripciones-carreras")
    public ResponseEntity listarInscripcionesCarreras() {
        List<Resource<InscripcionCarrera>> inscripciones = servicioInscripciones.listarInscripcionesCarreras().stream()
                .map(inscripcionCarrera -> new Resource<>(inscripcionCarrera, generarLinks(inscripcionCarrera)))
                .collect(Collectors.toList());

        Resources<Resource<InscripcionCarrera>> respuesta = new Resources<>(inscripciones,
                linkTo(methodOn(RecursoInscripcionesCarreras.class).listarInscripcionesCarreras()).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping({"/carreras/{idCarrera}/alumnos/{idAlumno}", "/alumnos/{idAlumno}/carreras/{idCarrera}"})
    public ResponseEntity inscribirCarrera(@RequestBody InscripcionCarrera inscripcionCarrera,
                                         @PathVariable Integer idCarrera,
                                         @PathVariable Integer idAlumno) {

        inscripcionCarrera.setAlumno(servicioAlumnos.obtenerAlumno(idAlumno));
        inscripcionCarrera.setCarrera(servicioCarreras.obtenerCarrera(idCarrera));
        if(inscripcionCarrera.getFechaInscripcion() == null) {
            inscripcionCarrera.setFechaInscripcion(LocalDate.now());
        }

        InscripcionCarrera inscripcionGuardada = servicioInscripciones.inscribirCarrera(inscripcionCarrera);

        Link linkPropio = linkTo(methodOn(RecursoInscripcionesCarreras.class).obtenerInscripcionCarrera(idCarrera, idAlumno)).withSelfRel();
        Resource<InscripcionCarrera> respuesta = new Resource<>(inscripcionGuardada, generarLinks(inscripcionGuardada));
        return ResponseEntity.created(URI.create(linkPropio.getHref())).body(respuesta);
    }

    private List<Link> generarLinks(InscripcionCarrera inscripcionCarrera) {
        ArrayList<Link> links = new ArrayList<>();
        if (!inscripcionCarrera.getAlumno().hasLinks()) {
            inscripcionCarrera.getAlumno().add(
                    linkTo(methodOn(RecursoAlumnos.class).obtenerAlumno(inscripcionCarrera.getAlumno().getIdentificador())).withSelfRel(),
                    linkTo(methodOn(RecursoAlumnos.class).listarAlumnos()).withRel("alumnos"));
        }
        if (!inscripcionCarrera.getCarrera().hasLinks()) {
            inscripcionCarrera.getCarrera().add(
                    linkTo(methodOn(RecursoCarreras.class).obtenerCarrera(inscripcionCarrera.getCarrera().getIdentificador())).withSelfRel(),
                    linkTo(methodOn(RecursoCarreras.class).listarCarreras()).withRel("carreras"));
        }
        links.add(linkTo(methodOn(RecursoInscripcionesCarreras.class).obtenerInscripcionCarrera(
                inscripcionCarrera.getCarrera().getIdentificador(),
                inscripcionCarrera.getAlumno().getIdentificador()))
                .withSelfRel());
        links.add(linkTo(methodOn(RecursoInscripcionesCarreras.class).listarInscripcionesCarreras()).withRel("inscripciones-carreras"));

        return links;
    }

}
