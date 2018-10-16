package com.tecso.rest_api.resources;

import com.tecso.rest_api.entity.Carrera;
import com.tecso.rest_api.service.ServicioCarreras;
import com.tecso.rest_api.util.LinksHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
            LinksHelper.agregarLinks(carrera);
            Resource<Carrera> respuesta = new Resource<>(carrera);
            return ResponseEntity.ok(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity listarCarreras() {
        List<Resource<Carrera>> carreras = servicioCarreras.listarCarrera().stream()
                .map(carrera -> {
                    LinksHelper.agregarLinks(carrera);
                    return new Resource<>(carrera);
                })
                .collect(Collectors.toList());

        Resources<Resource<Carrera>> respuesta = new Resources<>(carreras,
                linkTo(methodOn(RecursoCarreras.class).listarCarreras()).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity agregarCarrera(@RequestBody Carrera carrera) {
        Carrera carreraAgregada = servicioCarreras.guardarCarrera(carrera);
        LinksHelper.agregarLinks(carreraAgregada);
        Resource<Carrera> respuesta = new Resource<>(carreraAgregada);
        return ResponseEntity.created(URI.create(carreraAgregada.getLink("self").getHref())).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity modificarCarrera(@RequestBody Carrera carrera, @PathVariable Integer id) {
        Carrera carreraModificada = servicioCarreras.modificarCarrera(carrera, id);

        if(carreraModificada == null) {
            return ResponseEntity.notFound().build();
        } else {
            LinksHelper.agregarLinks(carreraModificada);
            Resource<Carrera> respuesta = new Resource<>(carreraModificada);
            return ResponseEntity.ok(respuesta);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarCarrera(@PathVariable Integer id) {
        servicioCarreras.eliminarCarrera(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
