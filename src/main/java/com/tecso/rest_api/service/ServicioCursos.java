package com.tecso.rest_api.service;

import com.tecso.rest_api.db.RepositorioCarrera;
import com.tecso.rest_api.db.RepositorioCurso;
import com.tecso.rest_api.db.RepositorioProfesor;
import com.tecso.rest_api.entity.Curso;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import java.util.List;

@ManagedBean
public class ServicioCursos {

    @Autowired
    RepositorioCurso repositorioCurso;

    @Autowired
    RepositorioProfesor repositorioProfesor;

    @Autowired
    RepositorioCarrera repositorioCarrera;

    public Curso obtenerCurso(Integer id) {
        return repositorioCurso.findOne(id);
    }

    public List<Curso> listarCursos() {
        return repositorioCurso.findAll();
    }

    public Curso guardarCurso(Curso curso) {
        //TODO agregar json mapping customizado en controlador para traducir URIs en entidades para objetos anidados (sin usar spring data rest)
        curso.setProfesor(repositorioProfesor.findOne(curso.getProfesor().getIdentificador()));
        curso.setCarrera(repositorioCarrera.findOne(curso.getCarrera().getIdentificador()));
        return repositorioCurso.save(curso);
    }

    public Curso modificarCurso(Curso curso, Integer id) {
        if (repositorioCurso.findOne(id) == null) {
            return null;
        }
        curso.setIdentificador(id);
        return repositorioCurso.save(curso);
    }

    public void eliminarCurso(Integer id) {
        repositorioCurso.delete(id);
    }
}
