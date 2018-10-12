package com.tecso.rest_api.service;

import com.tecso.rest_api.db.RepositorioCarrera;
import com.tecso.rest_api.entity.Carrera;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import java.util.List;

@ManagedBean
public class ServicioCarreras {

    @Autowired
    RepositorioCarrera repositorioCarrera;

    public Carrera obtenerCarrera(Integer id) {
        return repositorioCarrera.findOne(id);
    }

    public List<Carrera> listarCarrera() {
        return repositorioCarrera.findAll();
    }

    public Carrera guardarCarrera(Carrera carrera) {
        return repositorioCarrera.save(carrera);
    }

    public Carrera modificarCarrera(Carrera carrera, Integer id) {
        if (repositorioCarrera.findOne(id) == null) {
            return null;
        }
        carrera.setIdentificador(id);
        return repositorioCarrera.save(carrera);
    }

    public void eliminarCarrera(Integer id) {
        repositorioCarrera.delete(id);
    }
}
