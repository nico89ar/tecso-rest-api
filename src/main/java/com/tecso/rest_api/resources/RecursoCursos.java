package com.tecso.rest_api.resources;

import com.tecso.rest_api.entity.Alumno;
import com.tecso.rest_api.entity.Curso;
import com.tecso.rest_api.response.AlumnosEnCursoResponse;
import com.tecso.rest_api.service.ServicioCursos;
import com.tecso.rest_api.service.ServicioInscripciones;
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
            Resource<Curso> respuesta = new Resource<>(curso, generarLinks(curso));
            return ResponseEntity.ok(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity listarCursos() {
        List<Resource<Curso>> cursos = servicioCursos.listarCursos().stream()
                .map(curso -> new Resource<>(curso, generarLinks(curso)))
                .collect(Collectors.toList());

        Resources<Resource<Curso>> respuesta = new Resources<>(cursos,
                linkTo(methodOn(RecursoCursos.class).listarCursos()).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}/alumnos")
    public ResponseEntity listarAlumnosInscriptos(@PathVariable Integer id) {
        Curso curso = servicioCursos.obtenerCurso(id);
        List<Alumno> alumnos  = servicioInscripciones.listarAlumnosInscriptosEnCurso(curso);
        for (Alumno alumno : alumnos) {
            alumno.add(linkTo(methodOn(RecursoAlumnos.class).obtenerAlumno(alumno.getIdentificador())).withSelfRel(),
                    linkTo(methodOn(RecursoAlumnos.class).listarAlumnos()).withRel("alumnos"));
        }
        curso.getProfesor().add(
                linkTo(methodOn(RecursoProfesores.class).obtenerProfesor(curso.getProfesor().getIdentificador())).withSelfRel(),
                linkTo(methodOn(RecursoProfesores.class).listarProfesores()).withRel("profesores"));
        curso.getCarrera().add(
                linkTo(methodOn(RecursoCarreras.class).obtenerCarrera(curso.getCarrera().getIdentificador())).withSelfRel(),
                linkTo(methodOn(RecursoCarreras.class).listarCarreras()).withRel("carreras"));
        Resource<AlumnosEnCursoResponse> respuesta = new Resource<>(new AlumnosEnCursoResponse(curso, alumnos),
                linkTo(methodOn(RecursoCursos.class).listarAlumnosInscriptos(curso.getIdentificador())).withSelfRel());
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity agregarCurso(@RequestBody Curso curso) {
        Curso cursoAgregado = servicioCursos.guardarCurso(curso);
        Link linkPropio = linkTo(methodOn(RecursoCursos.class).obtenerCurso(cursoAgregado.getIdentificador())).withSelfRel();
        Resource<Curso> respuesta = new Resource<>(cursoAgregado, generarLinks(cursoAgregado));
        return ResponseEntity.created(URI.create(linkPropio.getHref())).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity modificarCurso(@RequestBody Curso curso, @PathVariable Integer id) {
        Curso cursoModificado = servicioCursos.modificarCurso(curso, id);

        if(cursoModificado == null) {
            return ResponseEntity.notFound().build();
        } else {
            Resource<Curso> respuesta = new Resource<>(cursoModificado, generarLinks(curso));
            return ResponseEntity.ok(respuesta);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarCurso(@PathVariable Integer id) {
        servicioCursos.eliminarCurso(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private List<Link> generarLinks(Curso curso) {
        ArrayList<Link> links = new ArrayList<>();
        if (!curso.getProfesor().hasLinks()) {
            curso.getProfesor().add(
                    linkTo(methodOn(RecursoProfesores.class).obtenerProfesor(curso.getProfesor().getIdentificador())).withSelfRel(),
                    linkTo(methodOn(RecursoProfesores.class).listarProfesores()).withRel("profesores"));
        }
        if (!curso.getCarrera().hasLinks()) {
            curso.getCarrera().add(
                    linkTo(methodOn(RecursoCarreras.class).obtenerCarrera(curso.getCarrera().getIdentificador())).withSelfRel(),
                    linkTo(methodOn(RecursoCarreras.class).listarCarreras()).withRel("carreras"));
        }
        links.add(linkTo(methodOn(RecursoCursos.class).obtenerCurso(curso.getIdentificador())).withSelfRel());
        links.add(linkTo(methodOn(RecursoCursos.class).listarCursos()).withRel("cursos"));

        return links;
    }
}
