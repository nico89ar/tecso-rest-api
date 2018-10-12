package com.tecso.rest_api.db;

import com.tecso.rest_api.entity.InscripcionCarrera;
import com.tecso.rest_api.entity.InscripcionCarreraId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioInscripcionCarrera extends JpaRepository<InscripcionCarrera, InscripcionCarreraId> {
}
