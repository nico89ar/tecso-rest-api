package com.tecso.rest_api.db;

import com.tecso.rest_api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositorioInscripcionCurso extends JpaRepository<InscripcionCurso, InscripcionCursoId> {

    @Query("select inscripcion.alumno from InscripcionCurso inscripcion where inscripcion.curso = ?1")
    List<Alumno> findAlumnosByCurso(Curso curso);

    List<InscripcionCurso> findInscripcionCursosByAlumnoAndCurso_Carrera(Alumno alumno, Carrera carrera);
}
