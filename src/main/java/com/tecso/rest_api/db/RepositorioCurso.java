package com.tecso.rest_api.db;

import com.tecso.rest_api.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioCurso extends JpaRepository<Curso, Integer> {
}
