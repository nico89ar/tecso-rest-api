package com.tecso.rest_api.resources;

import com.tecso.rest_api.entity.Carrera;
import com.tecso.rest_api.service.ServicioCarreras;
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
@RequestMapping(value = "/carreras", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
public class RecursoCarreras {

    @Autowired
    ServicioCarreras servicioCarreras;

    @GetMapping("/{id}")
    public ResponseEntity obtenerCarrera(@PathVariable Integer id) {
        Carrera carrera = servicioCarreras.obtenerCarrera(id);

        if (carrera == null) {
            return ResponseEntity.notFound().build();
        } else {
            Resource<Carrera> respuesta = new Resource<>(carrera, generarLinks(carrera));
            return ResponseEntity.ok(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity listarCarreras() {
        List<Resource<Carrera>> carreras = servicioCarreras.listarCarrera().stream()
                .map(carrera -> new Resource<>(carrera, generarLinks(carrera)))
                .collect(Collectors.toList());

        Resources<Resource<Carrera>> respuesta = new Resources<>(carreras,
                linkTo(methodOn(RecursoCarreras.class).listarCarreras()).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity agregarCarrera(@RequestBody Carrera carrera) {
        Carrera carreraAgregada = servicioCarreras.guardarCarrera(carrera);
        Link linkPropio = linkTo(methodOn(RecursoCarreras.class).obtenerCarrera(carreraAgregada.getIdentificador())).withSelfRel();
        Resource<Carrera> respuesta = new Resource<>(carreraAgregada, generarLinks(carreraAgregada));
        return ResponseEntity.created(URI.create(linkPropio.getHref())).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity modificarCarrera(@RequestBody Carrera carrera, @PathVariable Integer id) {
        Carrera carreraModificada = servicioCarreras.modificarCarrera(carrera, id);

        if(carreraModificada == null) {
            return ResponseEntity.notFound().build();
        } else {
            Resource<Carrera> respuesta = new Resource<>(carreraModificada, generarLinks(carrera));
            return ResponseEntity.ok(respuesta);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarCarrera(@PathVariable Integer id) {
        servicioCarreras.eliminarCarrera(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private List<Link> generarLinks(Carrera carrera) {
        ArrayList<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(RecursoCarreras.class).obtenerCarrera(carrera.getIdentificador())).withSelfRel());
        links.add(linkTo(methodOn(RecursoCarreras.class).listarCarreras()).withRel("carrera"));
        return links;
    }
}
