package com.tecso.rest_api.db;

import com.tecso.rest_api.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.ManagedBean;

public interface RepositorioCarrera extends JpaRepository<Carrera, Integer> {
}
