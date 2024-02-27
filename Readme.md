##Ventas APIRest 

Implementación de SpringBoot con JPA

###Dependencias

-Spring Web
-Spring data JPA 
-Lombok
-MySQL Driver 

##Routes 

###Cliente

-post: http://localhost:8080/cliente/alta
-get: http://localhost:8080/cliente/todosclientes
-get: http://localhost:8080/cliente/id
-put: http://localhost:8080/cliente/modificar/id
-delete: http://localhost:8080/cliente/eliminar/id 

###Productos 

-post:http://localhost:8080/productos/altaproductos
-get:http://localhost:8080/productos/productos
-get: http://localhost:8080/productos/id
-put:http://localhost:8080/productos/modificarproductos/id
-delete: http://localhost:8080/productos/bajaproductos/id

###Ventas 

-post: http://localhost:8080/ventas/crear
-get: http://localhost:8080/ventas/todos
-get: http://localhost:8080/ventas/id 
-get: http://localhost:8080/ventas/id/comprobante
-delete: http://localhost:8080/ventas/id

###Swagger

-http://localhost:8080/swagger-ui/index.html


###Postman 

-Postman scripts

