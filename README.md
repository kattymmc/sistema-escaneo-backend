# Peticiones http para cada URL
## GET
* http://localhost:8080/api/documentos -> Ver todos los documentos
* http://localhost:8080/api/documentos/{id} -> Ver un documento según su id
* http://localhost:8080/api/documentos/filtrar/{term} -> Buscar según código de documento
* http://localhost:8080/api/tipo-documento/{id} -> Listar tipo de documento según su id
* http://localhost:8080/api/uploads/img/{nombreImagen:.+} -> Obetener una imagen según su nombre

## POST
* http://localhost:8080/api/documentos -> Registrar un documento
* http://localhost:8080/api/tipo-documento -> Registrar un tipo de documento
* http://localhost:8080/api/documentos/upload -> Subir una o varias imagenes
* http://localhost:8080/api/usuarios/register -> Registrar un usuario
* http://localhost:8080/api/autenticacion -> Inicio de sesión de un usuario

## PUT
* http://localhost:8080/api/documentos/{id} -> Actualizar un documento según su id
* http://localhost:8080/api/tipo-documento/{id} -> Actualizar tipo de documento según su id

## DELETE
* http://localhost:8080/api/documentos/{id} -> Eliminar un documento según su id
* http://localhost:8080/api/tipo-documento/{id} -> Eliminar un tipo de documento según su id
* http://localhost:8080/api/uploads/img/{id} -> Eliminar una imagen según su id