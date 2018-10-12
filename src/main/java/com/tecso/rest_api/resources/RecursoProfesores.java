package com.tecso.rest_api.resources;

import com.tecso.rest_api.entity.Profesor;
import com.tecso.rest_api.service.ServicioProfesores;
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
@RequestMapping(value = "/profesores", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
public class RecursoProfesores {

    @Autowired
    ServicioProfesores servicioProfesores;

    @GetMapping("/{id}")
    public ResponseEntity obtenerProfesor(@PathVariable Integer id) {
        Profesor profesor = servicioProfesores.obtenerProfesor(id);

        if (profesor == null) {
            return ResponseEntity.notFound().build();
        } else {
            Resource<Profesor> respuesta = new Resource<>(profesor, generarLinks(profesor));
            return ResponseEntity.ok(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity listarProfesores() {
        List<Resource<Profesor>> profesores = servicioProfesores.listarProfesores().stream()
                .map(profesor -> new Resource<>(profesor, generarLinks(profesor)))
                .collect(Collectors.toList());

        Resources<Resource<Profesor>> respuesta = new Resources<>(profesores,
                linkTo(methodOn(RecursoProfesores.class).listarProfesores()).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }


    @PostMapping
    public ResponseEntity agregarProfesor(@RequestBody Profesor profesor) {
        Profesor profesorAgregado = servicioProfesores.guardarProfesor(profesor);
        Link linkPropio = linkTo(methodOn(RecursoProfesores.class).obtenerProfesor(profesorAgregado.getIdentificador())).withSelfRel();
        Resource<Profesor> respuesta = new Resource<>(profesorAgregado, generarLinks(profesorAgregado));
        return ResponseEntity.created(URI.create(linkPropio.getHref())).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity modificarProfesor(@RequestBody Profesor profesor, @PathVariable Integer id) {
        Profesor profesorModificado = servicioProfesores.modificarProfesor(profesor, id);

        if(profesorModificado == null) {
            return ResponseEntity.notFound().build();
        } else {
            Resource<Profesor> respuesta = new Resource<>(profesorModificado, generarLinks(profesor));
            return ResponseEntity.ok(respuesta);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarProfesor(@PathVariable Integer id) {
        servicioProfesores.eliminarProfesor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private List<Link> generarLinks(Profesor profesor) {
        ArrayList<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(RecursoProfesores.class).obtenerProfesor(profesor.getIdentificador())).withSelfRel());
        links.add(linkTo(methodOn(RecursoProfesores.class).listarProfesores()).withRel("profesores"));
        return links;
    }
}
