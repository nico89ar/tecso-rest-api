insert into persona(identificador, nombre, apellido, tipo_documento, documento, fechanac)
values (1, 'Nicolas', 'Balbuena', 'DNI', 34795529, '1989-12-17')
on conflict do nothing;
insert into persona(identificador, nombre, apellido, tipo_documento, documento, fechanac)
values (2, 'Juan', 'Perez', 'DNI', 34581469, '1990-11-10')
on conflict do nothing;
insert into persona(identificador, nombre, apellido, tipo_documento, documento, fechanac)
values (3, 'Pedro', 'Gomez', 'DNI', 34778120, '1989-08-05')
on conflict do nothing;
insert into persona(identificador, nombre, apellido, tipo_documento, documento, fechanac)
values (4, 'Mauro', 'Gimenez', 'DNI', 34112589, '1989-09-17')
on conflict do nothing;
insert into persona(identificador, nombre, apellido, tipo_documento, documento, fechanac)
values (5, 'Miguel', 'Ramirez', 'DNI', 34115793, '1990-12-02')
on conflict do nothing;
insert into persona(identificador, nombre, apellido, tipo_documento, documento, fechanac)
values (6, 'Raul', 'Mendez', 'DNI', 34119741, '1988-02-07')
on conflict do nothing;

insert into alumno(identificador, legajo)
values (1, 17249)
on conflict do nothing;
insert into alumno(identificador, legajo)
values (2, 15847)
on conflict do nothing;
insert into alumno(identificador, legajo)
values (3, 13554)
on conflict do nothing;

insert into profesor(identificador, legajo)
values (4, 14112)
on conflict do nothing;
insert into profesor(identificador, legajo)
values (5, 16547)
on conflict do nothing;
insert into profesor(identificador, legajo)
values (6, 17125)
on conflict do nothing;

insert into carrera(identificador, nombre, descripcion, fechadesde, fechahasta)
values (1, 'ingenieria quimica', null, '1960-01-01', null)
on conflict do nothing;
insert into carrera(identificador, nombre, descripcion, fechadesde, fechahasta)
values (2, 'ingenieria en sistemas', null, '1970-01-01', null)
on conflict do nothing;

insert into curso(identificador, nombre, descripcion, anio, cupomaximo, idcarrera, idprofesor)
values (1, 'analisis matematico', null, 2018, 10, 1, 4)
on conflict do nothing;
insert into curso(identificador, nombre, descripcion, anio, cupomaximo, idcarrera, idprofesor)
values (2, 'sistemas operativos', null, 2018, 12, 2, 5)
on conflict do nothing;
insert into curso(identificador, nombre, descripcion, anio, cupomaximo, idcarrera, idprofesor)
values (3, 'algebra', null, 2017, 10, 1, 6)
on conflict do nothing;

insert into inscripciones_carrera(idalumno, idcarrera, fechainscripcion)
values(1, 2, '2015-01-01')
on conflict do nothing;
insert into inscripciones_carrera(idalumno, idcarrera, fechainscripcion)
values(2, 1, '2014-01-01')
on conflict do nothing;
insert into inscripciones_carrera(idalumno, idcarrera, fechainscripcion)
values(3, 1, '2017-01-01')
on conflict do nothing;

insert into inscripciones_curso(idalumno, idcurso, fechainscripcion, nota)
values (1, 2, '2018-01-01', null)
on conflict do nothing;
insert into inscripciones_curso(idalumno, idcurso, fechainscripcion, nota)
values (2, 1, '2018-01-01', null)
on conflict do nothing;
insert into inscripciones_curso(idalumno, idcurso, fechainscripcion, nota)
values (3, 1, '2018-01-01', null)
on conflict do nothing;
insert into inscripciones_curso(idalumno, idcurso, fechainscripcion, nota)
values (2, 3, '2017-01-01', 9)
on conflict do nothing;
insert into inscripciones_curso(idalumno, idcurso, fechainscripcion, nota)
values (3, 3, '2017-01-01', 7)
on conflict do nothing;