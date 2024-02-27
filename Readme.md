## Ventas API Rest

Implementación de SpringBoot con JPA para gestionar ventas, clientes y productos.

### Dependencias

- Spring Web
- Spring Data JPA
- Lombok
- MySQL Driver 

### Endpoints

### Cliente

- **POST**: [http://localhost:8080/cliente/alta](http://localhost:8080/cliente/alta) - Alta de un cliente
- **GET**: [http://localhost:8080/cliente/todosclientes](http://localhost:8080/cliente/todosclientes) - Obtener todos los clientes
- **GET**: [http://localhost:8080/cliente/id](http://localhost:8080/cliente/id) - Obtener un cliente por su ID
- **PUT**: [http://localhost:8080/cliente/modificar/id](http://localhost:8080/cliente/modificar/id) - Modificar un cliente por su ID
- **DELETE**: [http://localhost:8080/cliente/eliminar/id](http://localhost:8080/cliente/eliminar/id) - Eliminar un cliente por su ID

#### Productos

- **POST**: [http://localhost:8080/productos/altaproductos](http://localhost:8080/productos/altaproductos) - Alta de un producto
- **GET**: [http://localhost:8080/productos/productos](http://localhost:8080/productos/productos) - Obtener todos los productos
- **GET**: [http://localhost:8080/productos/id](http://localhost:8080/productos/id) - Obtener un producto por su ID
- **PUT**: [http://localhost:8080/productos/modificarproductos/id](http://localhost:8080/productos/modificarproductos/id) - Modificar un producto por su ID
- **DELETE**: [http://localhost:8080/productos/bajaproductos/id](http://localhost:8080/productos/bajaproductos/id) - Eliminar un producto por su ID

#### Ventas

- **POST**: [http://localhost:8080/ventas/crear](http://localhost:8080/ventas/crear) - Crear una venta
- **GET**: [http://localhost:8080/ventas/todos](http://localhost:8080/ventas/todos) - Obtener todas las ventas
- **GET**: [http://localhost:8080/ventas/id](http://localhost:8080/ventas/id) - Obtener una venta por su ID
- **GET**: [http://localhost:8080/ventas/id/comprobante](http://localhost:8080/ventas/id/comprobante) - Obtener el comprobante de una venta por su ID
- **DELETE**: [http://localhost:8080/ventas/id](http://localhost:8080/ventas/id) - Eliminar una venta por su ID

### Documentación

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Postman

- Colección de Postman y scripts disponibles.