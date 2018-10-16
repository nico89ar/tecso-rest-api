package com.tecso.rest_api.resources;

import com.tecso.rest_api.entity.Alumno;
import com.tecso.rest_api.entity.Curso;
import com.tecso.rest_api.response.AlumnosEnCursoResponse;
import com.tecso.rest_api.service.ServicioCursos;
import com.tecso.rest_api.service.ServicioInscripciones;
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
@RequestMapping(value = "/cursos", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
public class RecursoCursos {

    @Autowired
    ServicioCursos servicioCursos;

    @Autowired
    ServicioInscripciones servicioInscripciones;

    @GetMapping("/{id}")
    public ResponseEntity obtenerCurso(@PathVariable Integer id) {
        Curso curso = servicioCursos.obtenerCurso(id);

        if (curso == null) {
            return ResponseEntity.notFound().build();
        } else {
            LinksHelper.agregarLinks(curso);
            Resource<Curso> respuesta = new Resource<>(curso);
            return ResponseEntity.ok(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity listarCursos() {
        List<Resource<Curso>> cursos = servicioCursos.listarCursos().stream()
                .map(curso -> {
                    LinksHelper.agregarLinks(curso);
                    return new Resource<>(curso);
                })
                .collect(Collectors.toList());

        Resources<Resource<Curso>> respuesta = new Resources<>(cursos,
                linkTo(methodOn(RecursoCursos.class).listarCursos()).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}/alumnos")
    public ResponseEntity listarAlumnosInscriptos(@PathVariable Integer id) {
        Curso curso = servicioCursos.obtenerCurso(id);
        List<Alumno> alumnos  = servicioInscripciones.listarAlumnosInscriptosEnCurso(curso);

        AlumnosEnCursoResponse alumnosEnCursoResponse = new AlumnosEnCursoResponse(curso, alumnos);
        LinksHelper.agregarLinks(alumnosEnCursoResponse);

        Resource<AlumnosEnCursoResponse> respuesta = new Resource<>(alumnosEnCursoResponse);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity agregarCurso(@RequestBody Curso curso) {
        Curso cursoAgregado = servicioCursos.guardarCurso(curso);
        LinksHelper.agregarLinks(cursoAgregado);
        Resource<Curso> respuesta = new Resource<>(cursoAgregado);
        return ResponseEntity.created(URI.create(cursoAgregado.getLink("self").getHref())).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity modificarCurso(@RequestBody Curso curso, @PathVariable Integer id) {
        Curso cursoModificado = servicioCursos.modificarCurso(curso, id);

        if(cursoModificado == null) {
            return ResponseEntity.notFound().build();
        } else {
            LinksHelper.agregarLinks(cursoModificado);
            Resource<Curso> respuesta = new Resource<>(cursoModificado);
            return ResponseEntity.ok(respuesta);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarCurso(@PathVariable Integer id) {
        servicioCursos.eliminarCurso(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
