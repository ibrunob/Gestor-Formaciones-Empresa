package dev.brunob.ProyectoBase2025;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.brunob.ProyectoBase2025.modelo.Asistencia;
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.Evaluacion;
import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.modelo.Profesor;
import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.repositorios.AsistenciaRepository;
import dev.brunob.ProyectoBase2025.repositorios.EstudianteRepository;
import dev.brunob.ProyectoBase2025.repositorios.EvaluacionRepository;
import dev.brunob.ProyectoBase2025.repositorios.FormacionEmpresaRepository;
import dev.brunob.ProyectoBase2025.services.AsistenciaService;
import dev.brunob.ProyectoBase2025.services.EstudianteService;
import dev.brunob.ProyectoBase2025.services.EvaluacionService;
import dev.brunob.ProyectoBase2025.services.FormacionEmpresaService;

/**
 * Suite de pruebas unitarias que ejercita varios servicios del sistema usando
 * Mockito para simular los repositorios. Verifica el comportamiento de
 * EstudianteService, FormacionEmpresaService, EvaluacionService y
 * AsistenciaService sin necesidad de base de datos ni contexto Spring.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de los servicios del sistema FE")
class ServiciosTests {

    @Mock private EstudianteRepository estudianteRepository;
    @Mock private FormacionEmpresaRepository formacionEmpresaRepository;
    @Mock private EvaluacionRepository evaluacionRepository;
    @Mock private AsistenciaRepository asistenciaRepository;

    @InjectMocks private EstudianteService estudianteService;
    @InjectMocks private FormacionEmpresaService formacionEmpresaService;
    @InjectMocks private EvaluacionService evaluacionService;
    @InjectMocks private AsistenciaService asistenciaService;

    private Estudiante est;
    private Profesor prof;
    private Tutor tutor;

    @BeforeEach
    void setUp() {
        est = new Estudiante();
        est.setId(10L);
        est.setFirstName("Ana");
        est.setLastName("García");
        est.setEmail("ana@centro.es");

        prof = new Profesor();
        prof.setId(20L);
        prof.setFirstName("Luis");
        prof.setLastName("Pérez");

        tutor = new Tutor();
        tutor.setId(30L);
        tutor.setFirstName("María");
        tutor.setLastName("López");
    }

    // ----------------------- EstudianteService -----------------------

    @Test
    @DisplayName("EstudianteService.save delega en el repositorio y devuelve la entidad")
    void estudianteService_save_devuelveEntidad() {
        when(estudianteRepository.save(est)).thenReturn(est);

        Estudiante guardado = estudianteService.save(est);

        assertNotNull(guardado);
        assertEquals(10L, guardado.getId());
        verify(estudianteRepository, times(1)).save(est);
    }

    @Test
    @DisplayName("EstudianteService.find devuelve null cuando no existe el id")
    void estudianteService_find_devuelveNullSiNoExiste() {
        when(estudianteRepository.findById(99L)).thenReturn(Optional.empty());

        Estudiante r = estudianteService.find(99L);

        assertNull(r);
    }

    @Test
    @DisplayName("EstudianteService.findByEmail devuelve el estudiante esperado")
    void estudianteService_findByEmail() {
        when(estudianteRepository.findByEmail("ana@centro.es")).thenReturn(est);

        Estudiante r = estudianteService.findByEmail("ana@centro.es");

        assertNotNull(r);
        assertEquals("Ana", r.getFirstName());
    }

    // -------------------- FormacionEmpresaService --------------------

    @Test
    @DisplayName("FormacionEmpresaService.findByProfesor filtra por profesor")
    void formacionEmpresaService_findByProfesor() {
        FormacionEmpresa fe1 = new FormacionEmpresa();
        fe1.setEstudiante(est);
        fe1.setProfesor(prof);
        fe1.setEstado("EN_CURSO");

        when(formacionEmpresaRepository.findByProfesorId(20L))
                .thenReturn(Arrays.asList(fe1));

        List<FormacionEmpresa> r = formacionEmpresaService.findByProfesor(20L);

        assertEquals(1, r.size());
        assertEquals("EN_CURSO", r.get(0).getEstado());
        verify(formacionEmpresaRepository).findByProfesorId(20L);
    }

    @Test
    @DisplayName("FormacionEmpresaService.findByTutor devuelve lista vacía si no hay registros")
    void formacionEmpresaService_findByTutor_listaVacia() {
        when(formacionEmpresaRepository.findByTutorId(eq(30L)))
                .thenReturn(java.util.Collections.emptyList());

        List<FormacionEmpresa> r = formacionEmpresaService.findByTutor(30L);

        assertNotNull(r);
        assertTrue(r.isEmpty());
    }

    // ------------------------ EvaluacionService ----------------------

    @Test
    @DisplayName("EvaluacionService.save persiste evaluación con autor profesor")
    void evaluacionService_save_conProfesor() {
        Evaluacion ev = new Evaluacion();
        ev.setEstudiante(est);
        ev.setProfesor(prof);
        ev.setFecha(LocalDate.of(2026, 4, 30));
        ev.setActitud(5);
        ev.setPuntualidad(4);
        ev.setCompetencias(5);
        ev.setValoracionGeneral(5);
        ev.setObservaciones("Excelente progreso");

        when(evaluacionRepository.save(ev)).thenReturn(ev);

        Evaluacion guardada = evaluacionService.save(ev);

        assertNotNull(guardada);
        assertEquals(20L, guardada.getProfesor().getId());
        assertNull(guardada.getTutor());
        assertEquals(5, guardada.getValoracionGeneral());
    }

    @Test
    @DisplayName("EvaluacionService.findByEstudiante delega en el repositorio")
    void evaluacionService_findByEstudiante() {
        Evaluacion e1 = new Evaluacion();
        e1.setEstudiante(est);
        e1.setTutor(tutor);

        when(evaluacionRepository.findByEstudianteIdOrderByFechaDesc(10L))
                .thenReturn(Arrays.asList(e1));

        List<Evaluacion> r = evaluacionService.findByEstudiante(10L);

        assertEquals(1, r.size());
        assertEquals(30L, r.get(0).getTutor().getId());
    }

    // ------------------------ AsistenciaService ----------------------

    @Test
    @DisplayName("AsistenciaService.save crea un registro de asistencia válido")
    void asistenciaService_save() {
        Asistencia a = new Asistencia();
        a.setEstudiante(est);
        a.setFecha(LocalDate.of(2026, 4, 28));
        a.setPresente(false);
        a.setJustificada(true);
        a.setMotivo("Cita médica");
        a.setRegistradoPor(prof);

        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(a);

        Asistencia guardada = asistenciaService.save(a);

        assertNotNull(guardada);
        assertFalse(guardada.getPresente());
        assertTrue(guardada.getJustificada());
        assertEquals("Cita médica", guardada.getMotivo());
        verify(asistenciaRepository).save(a);
    }

    @Test
    @DisplayName("AsistenciaService.findByEstudiante devuelve el historial ordenado")
    void asistenciaService_findByEstudiante() {
        Asistencia a1 = new Asistencia();
        a1.setFecha(LocalDate.of(2026, 4, 28));
        a1.setPresente(true);
        Asistencia a2 = new Asistencia();
        a2.setFecha(LocalDate.of(2026, 4, 27));
        a2.setPresente(false);

        when(asistenciaRepository.findByEstudianteIdOrderByFechaDesc(10L))
                .thenReturn(Arrays.asList(a1, a2));

        List<Asistencia> r = asistenciaService.findByEstudiante(10L);

        assertEquals(2, r.size());
        assertTrue(r.get(0).getPresente());
        assertFalse(r.get(1).getPresente());
    }

    // ----------- Test de integración entre servicios (mocks) ---------

    @Test
    @DisplayName("Flujo combinado: profesor obtiene formaciones y registra una asistencia")
    void flujoIntegrado_profesorRegistraAsistencia() {
        // Dado: el profesor 20 tiene una formación con la estudiante 10
        FormacionEmpresa fe = new FormacionEmpresa();
        fe.setEstudiante(est);
        fe.setProfesor(prof);
        fe.setEstado("EN_CURSO");
        when(formacionEmpresaRepository.findByProfesorId(20L)).thenReturn(Arrays.asList(fe));

        // Cuando: el controlador (simulado) carga sus formaciones...
        List<FormacionEmpresa> fes = formacionEmpresaService.findByProfesor(20L);
        assertEquals(1, fes.size());
        Estudiante alumno = fes.get(0).getEstudiante();

        // ...y registra una falta para el alumno
        Asistencia nueva = new Asistencia();
        nueva.setEstudiante(alumno);
        nueva.setFecha(LocalDate.of(2026, 4, 30));
        nueva.setPresente(false);
        nueva.setJustificada(false);
        nueva.setRegistradoPor(prof);
        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(nueva);

        Asistencia persistida = asistenciaService.save(nueva);

        // Entonces: la asistencia queda persistida y referenciada al alumno correcto
        assertNotNull(persistida);
        assertEquals(10L, persistida.getEstudiante().getId());
        assertEquals(20L, persistida.getRegistradoPor().getId());
        assertFalse(persistida.getPresente());
        verify(formacionEmpresaRepository, times(1)).findByProfesorId(20L);
        verify(asistenciaRepository, times(1)).save(nueva);
    }
}
