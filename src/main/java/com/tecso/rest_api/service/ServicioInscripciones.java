package com.tecso.rest_api.service;

import com.tecso.rest_api.db.RepositorioInscripcionCarrera;
import com.tecso.rest_api.db.RepositorioInscripcionCurso;
import com.tecso.rest_api.entity.*;
import com.tecso.rest_api.exceptions.NoInscriptoEnCarreraException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import java.util.List;

@ManagedBean
public class ServicioInscripciones {

    @Autowired
    RepositorioInscripcionCarrera repositorioInscripcionCarrera;

    @Autowired
    RepositorioInscripcionCurso repositorioInscripcionCurso;

    public List<InscripcionCurso> listarInscripcionesCursos() {
        return repositorioInscripcionCurso.findAll();
    }

    public InscripcionCurso obtenerInscripcionCurso(InscripcionCursoId id) {
        return repositorioInscripcionCurso.findOne(id);
    }

    public InscripcionCurso inscribirCurso(InscripcionCurso inscripcionCurso) throws  NoInscriptoEnCarreraException {
        InscripcionCarreraId id = new InscripcionCarreraId(
                inscripcionCurso.getCurso().getCarrera().getIdentificador(),
                inscripcionCurso.getAlumno().getIdentificador()
        );

        if (repositorioInscripcionCarrera.findOne(id) == null) {
            throw  new NoInscriptoEnCarreraException();
        }
        return repositorioInscripcionCurso.save(inscripcionCurso);
    }

    public List<InscripcionCarrera> listarInscripcionesCarreras() {
        return repositorioInscripcionCarrera.findAll();
    }

    public List<Alumno> listarAlumnosInscriptosEnCurso(Curso curso) {
        return repositorioInscripcionCurso.findAlumnosByCurso(curso);
    }

    public InscripcionCarrera obtenerInscripcionCarrera(InscripcionCarreraId id) {
        return repositorioInscripcionCarrera.findOne(id);
    }

    public InscripcionCarrera inscribirCarrera(InscripcionCarrera inscripcionCarrera) {
        return repositorioInscripcionCarrera.save(inscripcionCarrera);
    }
}
