-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password,direccion, email, telefono, first_name, last_name)
VALUES (1, TRUE, 'ADMIN', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'calla jefazo', 'admin@faq.es', '123456321', 'admin', 'admin');

INSERT INTO IWUser (id, enabled, roles, username, password,direccion, email, telefono, first_name, last_name)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'calle machado', 'user@normalito.com', '456543675', 'user', 'user');

INSERT INTO IWUser (id, enabled, roles, username, password,direccion, email, telefono, first_name, last_name)
VALUES (3, TRUE, 'EMPLEADO', 'emp',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'calle empleado', 'emp1@emps.com', '123123123', 'empleado', 'empleado');

INSERT INTO IWUser (id, enabled, roles, username, password, direccion, email, telefono, first_name, last_name)
VALUES (4, TRUE, 'USER', 'u',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'calle lola', 'holita@ucm.es','678435232', 'user2', 'user2');

INSERT INTO IWUser (id, enabled, roles, username, password,direccion, email, telefono, first_name, last_name)
VALUES (5, TRUE, 'EMPLEADO', 'emp2',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'calle noseque', 'emp2@emps.com', '789789789', 'empleado2', 'empleado2');

INSERT INTO CATEGORIA (id, nombre, activo)
VALUES (0, 'Entrantes', 1);
INSERT INTO CATEGORIA (id, nombre, activo)
VALUES (1, 'Carnes', 1);
INSERT INTO CATEGORIA (id, nombre, activo)
VALUES (2, 'Ensaladas', 1);

INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo, popularidad)
VALUES (1, 'Croquetas como las de tu casa', 'Croquetas', 7.99, 0, 1, 5);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo, popularidad)
VALUES (2, 'Patas picantonas para los mas atrevidos', 'Patatas bravas', 5.99, 0, 1, 3);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo, popularidad)
VALUES (3, 'Nachos bien crujientes y deliciosos', 'Nachos', 4.99, 0, 1, 0);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo, popularidad)
VALUES (4, 'Aros de cebolla hechos con nuestro estilo original', 'Aros de cebolla', 4.99, 0, 1, 3);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo, popularidad)
VALUES (5, 'Fingers de queso que se derretiran en tu boca', 'Fingers de queso', 5.99, 0, 1, 2);

INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo, popularidad)
VALUES (6, 'Chuletas con nuestra salsa especial barbacoa', 'Chuletas barbacoa', 10.99, 1, 1, 5);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo, popularidad)
VALUES (7, 'Solomillo en su punto perfecto para ti', 'Solomillo', 10.99, 1, 1, 4);

INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo, popularidad)
VALUES (8, 'La ensalada del gran emperador romano', 'Cesar', 9.99, 2, 1, 1);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo, popularidad)
VALUES (9, 'Ensalada con un toque surfero', 'California', 10.99, 2, 1, 3);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo, popularidad)
VALUES (10, 'Nuestra ensalada mas afrodisiaca', 'Waikiki', 9.99, 2, 1, 2);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo, popularidad)
VALUES (11, 'El sabor de Italia en una ensalada', 'Toscana', 8.99, 2, 1, 0);

INSERT INTO RESERVA(id, activo, personas, cliente_id, fecha, mesas) 
VALUES(1, true, 8, 1, '2022-02-20T10:00:00', 2);
INSERT INTO RESERVA(id, activo, personas, cliente_id, fecha, mesas) 
VALUES(2, true, 5, 1, '2022-02-20T16:00:00', 2);
INSERT INTO RESERVA(id, activo, personas, cliente_id, fecha, mesas) 
VALUES(3, true, 5, 2, '2022-02-21T10:00:00', 2);
INSERT INTO RESERVA(id, activo, personas, cliente_id, fecha, mesas) 
VALUES(4, true, 5, 2, '2022-02-21T10:00:00', 2);
INSERT INTO RESERVA(id, activo, personas, cliente_id, fecha, mesas) 
VALUES(5, false, 5, 2, '2022-02-21T10:00:00', 2);

 INSERT INTO PEDIDO(id, activo, direccion, estado, cliente_id, fecha, express, is_take_away)
 VALUES(1, true, 'Calle Antilla', 0, 2, '2022-02-20T10:00:00',true, true);
 INSERT INTO PEDIDO(id, activo, direccion,  estado, cliente_id, fecha, express, is_take_away)
 VALUES(2, true, 'Calle Holanda', 0, 2, '2022-02-20T11:00:00', true, false);
 INSERT INTO PEDIDO(id, activo, direccion, estado, cliente_id, fecha, express, is_take_away)
 VALUES(3, true, 'Calle Eros', 0, 2,'2022-02-20T13:00:00',false, false);
 INSERT INTO PEDIDO(id, activo, direccion, estado, cliente_id, fecha, express, is_take_away)
 VALUES(4, true, 'Calle Cristina', 0, 2, '2022-02-22T10:00:00',false, false);
  INSERT INTO PEDIDO(id, activo, direccion, estado, cliente_id, fecha, express, is_take_away)
 VALUES(5, true, 'Calle A',  4, 2, '2022-02-25T10:00:00', false, false);
  INSERT INTO PEDIDO(id, activo, direccion, estado, cliente_id, fecha, express, is_take_away)
 VALUES(6, true, 'Calle B', 2, 2, '2022-02-27T10:00:00', false, false);
  INSERT INTO PEDIDO(id, activo, direccion, estado, cliente_id, fecha, express, is_take_away)
 VALUES(7, true, 'Calle C', 3, 2, '2022-02-26T10:00:00', false, false);
 INSERT INTO PEDIDO(id, activo, direccion, estado, cliente_id, fecha, express, is_take_away)
 VALUES(8, true, 'Calle C', 4, 2, '2022-02-27T10:00:00', false, false);


INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(1, 1, 1,  7.99);
INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(2, 1, 2, 5.99);
INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(3, 2, 2, 4.99);
INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(1, 2, 2, 7.99);
INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(2, 3, 2, 5.99);
INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(4, 4, 1,  4.99);
INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(5, 5, 2, 5.99);
INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(5, 6, 1, 5.99);
INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(1, 6, 1, 7.99);
INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(1, 7, 2, 7.99);

INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(5, 8, 1, 5.99);
INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(2, 8, 2, 5.99);
INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad, precio)
VALUES(8, 8, 1, 9.99);
 --INSERT INTO LINEA_PLATO_PEDIDO(plato_id, pedido_id, cantidad)
 --VALUES(1, 1, 2);

INSERT INTO CONFIGURACION_RESTAURANTE(ID,HORA_FIN,HORA_INI,MAX_PEDIDOS_HORA,MAX_RESERVAS,NOMBRE_SITIO,PERSONAS_MESA)
VALUES(1,22,9,20,10,'Restaurant.es',4);


INSERT INTO VALORACION(id, descripcion, rate, cliente_id, plato_id, activo)
VALUES (1, 'Me encantan', 4, 2, 1, 1);

INSERT INTO VALORACION(id, descripcion, rate, cliente_id, plato_id, activo)
VALUES (2, 'Podrian mejorar', 2, 1, 1, 1);

INSERT INTO VALORACION(id, descripcion, rate, cliente_id, plato_id, activo)
VALUES (3, 'Son las mejores que he comido nunca. Bien de cantidad y muy ricas. Repetire seguro', 5, 3, 1, 1);

ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;