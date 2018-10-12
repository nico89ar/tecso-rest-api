package com.tecso.rest_api.resources;

import com.tecso.rest_api.entity.Alumno;
import com.tecso.rest_api.service.ServicioAlumnos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @GetMapping("/{id}")
    public ResponseEntity obtenerAlumno(@PathVariable Integer id) {
        Alumno alumno = servicioAlumnos.obtenerAlumno(id);

        if (alumno == null) {
            return ResponseEntity.notFound().build();
        } else {
            Resource<Alumno> respuesta = new Resource<>(alumno, generarLinks(alumno));
            return ResponseEntity.ok(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity listarAlumnos() {
        List<Resource<Alumno>> alumnos = servicioAlumnos.listarAlumnos().stream()
                .map(alumno -> new Resource<>(alumno, generarLinks(alumno)))
                .collect(Collectors.toList());

        Resources<Resource<Alumno>> respuesta = new Resources<>(alumnos,
                linkTo(methodOn(RecursoAlumnos.class).listarAlumnos()).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity agregarAlumno(@RequestBody Alumno alumno) {
        Alumno alumnoAgregado = servicioAlumnos.guardarAlumno(alumno);
        Link linkPropio = linkTo(methodOn(RecursoAlumnos.class).obtenerAlumno(alumnoAgregado.getIdentificador())).withSelfRel();
        Resource<Alumno> respuesta = new Resource<>(alumnoAgregado, generarLinks(alumnoAgregado));
        return ResponseEntity.created(URI.create(linkPropio.getHref())).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity modificarAlumno(@RequestBody Alumno alumno, @PathVariable Integer id) {
        Alumno alumnoModificado = servicioAlumnos.modificarAlumno(alumno, id);

        if(alumnoModificado == null) {
            return ResponseEntity.notFound().build();
        } else {
            Resource<Alumno> respuesta = new Resource<>(alumnoModificado, generarLinks(alumno));
            return ResponseEntity.ok(respuesta);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarAlumno(@PathVariable Integer id) {
        servicioAlumnos.eliminarAlumno(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private List<Link> generarLinks(Alumno alumno) {
        ArrayList<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(RecursoAlumnos.class).obtenerAlumno(alumno.getIdentificador())).withSelfRel());
        links.add(linkTo(methodOn(RecursoAlumnos.class).listarAlumnos()).withRel("alumnos"));
        return links;
    }
}
