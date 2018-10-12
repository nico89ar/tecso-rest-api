package com.tecso.rest_api.service;

import com.tecso.rest_api.db.RepositorioAlumno;
import com.tecso.rest_api.entity.Alumno;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import java.util.List;

@ManagedBean
public class ServicioAlumnos {

    @Autowired
    RepositorioAlumno repositorioPersona;

    public Alumno obtenerAlumno(Integer id) {
        return repositorioPersona.findOne(id);
    }

    public List<Alumno> listarAlumnos() {
        return repositorioPersona.findAll();
    }

    public Alumno guardarAlumno(Alumno alumno) {
        return repositorioPersona.save(alumno);
    }

    public Alumno modificarAlumno(Alumno alumno, Integer id) {
        if (repositorioPersona.findOne(id) == null) {
            return null;
        }
        alumno.setIdentificador(id);
        return repositorioPersona.save(alumno);
    }

    public void eliminarAlumno(Integer id) {
        repositorioPersona.delete(id);
    }

}
