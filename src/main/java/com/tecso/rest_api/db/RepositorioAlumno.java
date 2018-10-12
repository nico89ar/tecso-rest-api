package com.tecso.rest_api.db;

import com.tecso.rest_api.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioAlumno extends JpaRepository<Alumno, Integer> {
}
