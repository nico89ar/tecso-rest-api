package com.tecso.rest_api.service;

import com.tecso.rest_api.db.RepositorioProfesor;
import com.tecso.rest_api.entity.Profesor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import java.util.List;

@ManagedBean
public class ServicioProfesores {

    @Autowired
    RepositorioProfesor repositorioProfesor;

    public Profesor obtenerProfesor(Integer id) {
        return repositorioProfesor.findOne(id);
    }

    public List<Profesor> listarProfesores() {
        return repositorioProfesor.findAll();
    }

    public Profesor guardarProfesor(Profesor profesor) {
        return repositorioProfesor.save(profesor);
    }

    public Profesor modificarProfesor(Profesor profesor, Integer id) {
        if (repositorioProfesor.findOne(id) == null) {
            return null;
        }
        profesor.setIdentificador(id);
        return repositorioProfesor.save(profesor);
    }

    public void eliminarProfesor(Integer id) {
        repositorioProfesor.delete(id);
    }

}
