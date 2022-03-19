-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (1, TRUE, 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');


INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (3, TRUE, 'EMPLEADO', 'emp',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO CATEGORIA (id, nombre, activo)
VALUES (0, 'Entrantes', 1);
INSERT INTO CATEGORIA (id, nombre, activo)
VALUES (1, 'Carnes', 1);
INSERT INTO CATEGORIA (id, nombre, activo)
VALUES (2, 'Ensaladas', 1);

INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo)
VALUES (0, 'Croquetas como las de tu casa', 'Croquetas', 7.99, 0, 1);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo)
VALUES (1, 'Patas picantonas para los mas atrevidos', 'Patatas bravas', 5.99, 0, 1);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo)
VALUES (2, 'Nachos bien crujientes y deliciosos', 'Nachos', 4.99, 0, 1);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo)
VALUES (3, 'Aros de cebolla hechos con nuestro estilo original', 'Aros de cebolla', 4.99, 0, 1);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo)
VALUES (4, 'Fingers de queso que se derretiran en tu boca', 'Fingers de queso', 5.99, 0, 1);

INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo)
VALUES (5, 'Chuletas con nuestra salsa especial barbacoa', 'Chuletas barbacoa', 10.99, 1, 1);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo)
VALUES (6, 'Solomillo en su punto perfecto para ti', 'Solomillo', 10.99, 1, 1);

INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo)
VALUES (7, 'La ensalada del gran emperador romano', 'Cesar', 9.99, 2, 1);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo)
VALUES (8, 'Ensalada con un toque surfero', 'California', 10.99, 2, 1);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo)
VALUES (9, 'Nuestra ensalada mas afrodisiaca', 'Waikiki', 9.99, 2, 1);
INSERT INTO PLATO(id, descripcion, nombre, precio, categoria_id, activo)
VALUES (10, 'El sabor de Italia en una ensalada', 'Toscana', 8.99, 2, 1);

INSERT INTO RESERVA(id, activo, personas, cliente_id) 
VALUES(1, true, 8, 1);
INSERT INTO RESERVA(id, activo, personas, cliente_id) 
VALUES(2, true, 5, 1);
INSERT INTO RESERVA(id, activo, personas, cliente_id) 
VALUES(3, true, 5, 2);

INSERT INTO PEDIDO(id, activo, direccion, en_curso, cliente_id)
VALUES(1, true, 'Calle Antilla', false, 2);

INSERT INTO LINEA_PLATO_PEDIDO(cantidad, precio, plato_id, pedido_id)
VALUES(2, 11.98, 1, 1);