Caso 5 - ReservaService - Tests 7 al 9
Integrante: Jeronimo Mejia - Tests 7, 8, 9
Archivo: src/test/java/com/proyecto/reservas/service/ReservaServiceTest.java

Test 7: test7_listarPorUsuario
Objetivo: Validar que se listan correctamente las reservas de un usuario.
Que se mockea: ReservaRepository.findByUsuarioId() retorna lista con 2 reservas.
Resultado esperado:   El servicio retorna una lista de tamaño 2, sin lanzar excepción.

@Test
    void test7_aprobarOk() {
        Reserva r = new Reserva(espacioOk, usuarioOk, LocalDate.now().plusDays(1), LocalTime.of(8,0), LocalTime.of(10,0), 10, "Test");
        setId(r, 1L);
        r.setEstado(EstadoReserva.PENDIENTE);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(r));
        when(reservaRepository.findByEspacioId(1L)).thenReturn(List.of());
        when(reservaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        assertEquals(EstadoReserva.APROBADA, reservaService.aprobar(1L).getEstado());
    }


Test 8: test8_cancelarOk
Objetivo: Validar el flujo feliz de cancelación.
Que se mockea: ReservaRepository.findById() retorna una reserva en estado ACTIVA.
Resultado esperado: El servicio cambia el estado a CANCELADA y guarda con save().

   @Test
    void test8_aprobarCruce() {
        Reserva r = new Reserva(espacioOk, usuarioOk, LocalDate.now().plusDays(1), LocalTime.of(8,0), LocalTime.of(10,0), 10, "Test");
        setId(r, 1L);
        r.setEstado(EstadoReserva.PENDIENTE);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(r));
        Reserva existente = new Reserva(espacioOk, usuarioOk, r.getFecha(), LocalTime.of(9,0), LocalTime.of(11,0), 5, "Otra");
        setId(existente, 2L);	
        existente.setEstado(EstadoReserva.APROBADA);
        when(reservaRepository.findByEspacioId(1L)).thenReturn(List.of(existente));
        assertThrows(ConflictoReservaException.class, () -> reservaService.aprobar(1L));
    }


Test 9: test9_cancelarNoExiste
Objetivo: Validar que no se puede cancelar una reserva inexistente.
Que se mockea: ReservaRepository.findById() retorna Optional.empty().
Resultado esperado: EntityNotFoundException con mensaje "Reserva no encontrada".

@Test
    void test9_aprobarNoPendiente() {
        Reserva r = new Reserva(espacioOk, usuarioOk, LocalDate.now().plusDays(1), LocalTime.of(8,0), LocalTime.of(10,0), 10, "Test");
        setId(r, 1L);
        r.setEstado(EstadoReserva.APROBADA);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(r));
        assertThrows(IllegalArgumentException.class, () -> reservaService.aprobar(1L));
    }

Evidencia
<img width="1789" height="615" alt="image" src="https://github.com/user-attachments/assets/8b19f8ce-77da-4f33-8f4f-0522096ec825" />

