Caso 5 - ReservaService - Pruebas 1 al 3 Integrante: Manuela Rodriguez Lopez Archivo:
src/test/java/com/proyecto/reservas/service/ReservaServiceTest.java


Test 4: test4_espacioNoEncontrado
Objetivo: Validar que no se puede crear una reserva si el espacio no existe.
Que se mockea: EspacioRepository.findById() retorna Optional.empty().
Resultado esperado: EntityNotFoundException con mensaje "Espacio no encontrado".

@Test
    void test4_cruce() {
        var req = reqValido();
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioOk));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioOk));
        Reserva existente = new Reserva(espacioOk, usuarioOk, req.fecha, LocalTime.of(9,0), LocalTime.of(11,0), 5, "Otra");
        existente.setEstado(EstadoReserva.APROBADA);
        when(reservaRepository.findByEspacioId(1L)).thenReturn(List.of(existente));
        assertThrows(ConflictoReservaException.class, () -> reservaService.crear(req));
    }


Test 5: test5_usuarioNoEncontrado
Objetivo: Validar que no se puede crear una reserva si el usuario no existe.
Que se mockea: EspacioRepository retorna espacio OK, pero UsuarioRepository.findById() retorna Optional.empty().
Resultado esperado: EntityNotFoundException con mensaje "Usuario no encontrado".

    @Test
    void test5_motivoBlank() {
        var req = reqValido();
        req.motivo = "   ";
        assertThrows(IllegalArgumentException.class, () -> reservaService.crear(req));
    }


Test 6: test6_conflictoDeHorario
Objetivo Validar la lógica de negocio de solapamiento de horarios.
Que se mockea: ReservaRepository.findByEspacioId() retorna una lista con 1 reserva existente de 8am a 10am.
Que se hace: Se intenta crear una nueva reserva de 9am

    @Test
    void test6_limiteExactoBordeNoCruce() {
        var req = reqValido();
        req.horaInicio = LocalTime.of(10,0);
        req.horaFin = LocalTime.of(12,0);
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioOk));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioOk));
        Reserva existente = new Reserva(espacioOk, usuarioOk, req.fecha, LocalTime.of(8,0), LocalTime.of(10,0), 5, "Otra");
        existente.setEstado(EstadoReserva.APROBADA);
        when(reservaRepository.findByEspacioId(1L)).thenReturn(List.of(existente));
        when(reservaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        assertDoesNotThrow(() -> reservaService.crear(req));

Foto: 
https://private-user-images.githubusercontent.com/265150406/692827748-85387f55-2a04-48a6-96e4-6ff67c6a9406.png?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3OTM3MTk2MTEsIm5iZiI6MTc5MzcxOTMxMSwicGF0aCI6Ii8yNjUxNTA0MDYvNjkyODI3NzQ4LTg1Mzg3ZjU1LTJhMDQtNDhhNi05NmU0LTZmZjY3YzZhOTQwNi5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjYxMTAzJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI2MTEwM1QxNTIxNTFaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0zNjY1YmE5OGE0ODEwNzM2OWIzZDE2YTcwZWJhMTBkYTc3OTA4M2ZkMDIyZDQyYWNmNDc0MjdmNDFlN2E3MjZhJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZyZXNwb25zZS1jb250ZW50LXR5cGU9aW1hZ2UlMkZwbmcifQ.nk9K6uErUR6R2l7uN-pmZJ_Y1-XhRX3Y7xoxEIauPZk

