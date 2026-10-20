# Proyecto Caso 5 - Gestión de reservas de espacios

Aplicación REST con Spring Boot, Java 17, Spring Data JPA y base de datos H2.
Está organizada por capas, siguiendo el ejemplo del profesor:

- `model`: entidades del sistema (Espacio, Usuario, Reserva).
- `repository`: acceso a datos.
- `service`: reglas de negocio y validaciones.
- `controller`: endpoints de la API REST.
- `exception`: excepciones personalizadas y manejo de errores.

## Ejecutar

1. Abrir la carpeta `Proyecto_Caso5_SpringBoot` en IntelliJ IDEA como proyecto Gradle.
2. Usar Java 17 o superior.
3. Ejecutar `com.proyecto.reservas.ReservasApplication`.
4. La aplicación estará disponible en `http://localhost:8080`.

La base H2 es en memoria; los datos se reinician cuando se detiene la aplicación.

## Endpoints principales

### Espacios
- `POST /espacios` crear espacio
- `GET /espacios` listar espacios
- `GET /espacios/{id}` consultar espacio
- `PUT /espacios/{id}` actualizar espacio
- `PATCH /espacios/{id}/mantenimiento?activo=true` marcar mantenimiento
- `DELETE /espacios/{id}` eliminar espacio

Ejemplo `POST /espacios`:
```json
{
  "nombre": "Laboratorio de Sistemas",
  "tipo": "LABORATORIO",
  "capacidad": 30,
  "cantidadEquipos": 30,
  "sonidoProfesional": false,
  "enMantenimiento": false
}
```
Para auditorio se puede usar `"tipo": "AUDITORIO"` y `sonidoProfesional: true`.

### Usuarios
- `POST /usuarios` registrar solicitante/responsable
- `GET /usuarios` listar usuarios
- `GET /usuarios/{id}` consultar usuario

Ejemplo `POST /usuarios`:
```json
{
  "nombre": "Laura Pérez",
  "correo": "laura@institucion.edu",
  "rol": "SOLICITANTE"
}
```

Roles admitidos: `SOLICITANTE`, `ADMINISTRADOR`, `COORDINADOR`, `LOGISTICA`, `SUPERVISOR`.

### Reservas
- `POST /reservas` crear solicitud en estado `PENDIENTE`
- `GET /reservas` listar reservas
- `GET /reservas/{id}` consultar reserva
- `PATCH /reservas/{id}/aprobar` aprobar una reserva
- `PATCH /reservas/{id}/rechazar?motivo=...` rechazar una reserva
- `PATCH /reservas/{id}/cancelar?motivo=...` cancelar una reserva (motivo obligatorio)

Ejemplo `POST /reservas`:
```json
{
  "espacioId": 1,
  "usuarioId": 1,
  "fecha": "2026-10-20",
  "horaInicio": "08:00",
  "horaFin": "10:00",
  "cantidadAsistentes": 20,
  "motivo": "Práctica de programación"
}
```

## Reglas implementadas

- La reserva siempre tiene espacio, usuario responsable, fecha, horas, cantidad y motivo.
- La hora final debe ser posterior a la inicial.
- Se valida capacidad; para laboratorios también se valida cantidad de equipos.
- Los espacios en mantenimiento no se pueden reservar.
- No se permiten cruces en el mismo espacio para reservas pendientes o aprobadas.
- Las reservas nuevas quedan en estado `PENDIENTE`.
- Solo se aprueban reservas pendientes y se vuelve a comprobar que no exista cruce.
- El rechazo y la cancelación guardan el motivo correspondiente.

## Consola H2

Abrir `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:reservasdb`
- User Name: `sa`
- Password: vacío

## Flujo

`Solicitud HTTP -> Controller -> Service -> Repository -> H2`
