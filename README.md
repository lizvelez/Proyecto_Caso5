Caso 5 - ReservaService - Tests 1 al 3
Integrante: Lizeth Velez Mendez
Archivo: src/test/java/com/proyecto/reservas/service/ReservaServiceTest.java

Test 1: test1_crearOk
Objetivo: Validar el flujo feliz de creación.
Que se mockea: EspacioRepository, UsuarioRepository devuelven existente, ReservaRepository.findByEspacioId devuelve lista vacía.
Resultado esperado: No lanza excepción y retorna reserva.

Código del test
@Test
void test1_crearOk() {
    var req = reqValido();
    when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioOk));
    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioOk));
    when(reservaRepository.findByEspacioId(1L)).thenReturn(List.of());
    when(reservaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    assertNotNull(reservaService.crear(req));
}

Test 2: test2_fechaInvalida
Objetivo: Validar que horaFin debe ser después de horaInicio.
Que se hace: Se envía req con horaFin 7am y horaInicio 8am.
Resultado esperado: IllegalArgumentException

Codigo del test
@Test
void test2_fechaInvalida() {
    var req = reqValido();
    req.horaFin = LocalTime.of(7,0); // 7am antes que 8am
    assertThrows(IllegalArgumentException.class, () -> reservaService.crear(req));
}

Test 3: test3_capacidad
Objetivo: Validar que el espacio no acepta más personas de las que soporta.
Que se mockea:espacioOk.validarCapacidad(100) = false
Resultado esperado: IllegalArgumentException

Código del test
@Test
void test3_capacidad() {
    var req = reqValido();
    req.cantidadAsistentes = 100;
    when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioOk));
    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioOk));
    when(espacioOk.validarCapacidad(100)).thenReturn(false);
    assertThrows(IllegalArgumentException.class, () -> reservaService.crear(req));
}

Evidencia
<img width="895" height="308" alt="image" src="https://github.com/user-attachments/assets/85387f55-2a04-48a6-96e4-6ff67c6a9406" />


