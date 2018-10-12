package com.tecso.rest_api.db;

import com.tecso.rest_api.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioProfesor extends JpaRepository<Profesor, Integer> {
}
