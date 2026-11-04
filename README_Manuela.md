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
<img width="895" height="308" alt="694220818-bbcf7f9e-535d-4d47-bc23-80d60bee4404" src="https://github.com/user-attachments/assets/4673580e-37eb-4e10-afe7-542847c06ad4" />


