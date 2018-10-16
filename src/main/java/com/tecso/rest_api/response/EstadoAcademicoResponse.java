package com.tecso.rest_api.response;

import com.tecso.rest_api.entity.Alumno;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadoAcademicoResponse extends ResourceSupport {

    private Alumno alumno;
    private List<EstadoCarrera> estadoCarreras;

}
