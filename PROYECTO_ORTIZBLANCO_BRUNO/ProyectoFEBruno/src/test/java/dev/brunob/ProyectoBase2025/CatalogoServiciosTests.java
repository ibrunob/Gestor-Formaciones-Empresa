package dev.brunob.ProyectoBase2025;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.brunob.ProyectoBase2025.modelo.CicloFormativo;
import dev.brunob.ProyectoBase2025.modelo.Curso;
import dev.brunob.ProyectoBase2025.modelo.Empresa;
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.repositorios.CicloFormativoRepository;
import dev.brunob.ProyectoBase2025.repositorios.CursoRepository;
import dev.brunob.ProyectoBase2025.repositorios.EmpresaRepository;
import dev.brunob.ProyectoBase2025.repositorios.EstudianteRepository;
import dev.brunob.ProyectoBase2025.repositorios.FormacionEmpresaRepository;
import dev.brunob.ProyectoBase2025.services.CicloFormativoService;
import dev.brunob.ProyectoBase2025.services.CursoService;
import dev.brunob.ProyectoBase2025.services.EmpresaService;
import dev.brunob.ProyectoBase2025.services.EstudianteService;
import dev.brunob.ProyectoBase2025.services.FormacionEmpresaService;

/**
 * Suite adicional de pruebas unitarias que cubre los servicios de catálogo
 * (Empresa, Curso, CicloFormativo) y operaciones extra de borrado, listado
 * y filtros del resto de servicios.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias extendidas de catálogo y operaciones CRUD")
class CatalogoServiciosTests {

    @Mock private EmpresaRepository empresaRepository;
    @Mock private CursoRepository cursoRepository;
    @Mock private CicloFormativoRepository cicloFormativoRepository;
    @Mock private EstudianteRepository estudianteRepository;
    @Mock private FormacionEmpresaRepository formacionEmpresaRepository;

    @InjectMocks private EmpresaService empresaService;
    @InjectMocks private CursoService cursoService;
    @InjectMocks private CicloFormativoService cicloFormativoService;
    @InjectMocks private EstudianteService estudianteService;
    @InjectMocks private FormacionEmpresaService formacionEmpresaService;

    // ============================== EMPRESA ==============================

    @Nested
    @DisplayName("EmpresaService")
    class EmpresaTests {

        private Empresa empresa;

        @BeforeEach
        void init() {
            empresa = new Empresa();
            empresa.setIdEmpresa(1L);
            empresa.setNombre("Acme S.L.");
            empresa.setDireccion("Calle Mayor 1");
        }

        @Test
        @DisplayName("save delega en el repositorio")
        void save() {
            when(empresaRepository.save(empresa)).thenReturn(empresa);
            Empresa r = empresaService.save(empresa);
            assertEquals("Acme S.L.", r.getNombre());
            verify(empresaRepository).save(empresa);
        }

        @Test
        @DisplayName("update reutiliza save del repositorio")
        void update() {
            empresa.setDireccion("Calle Nueva 99");
            when(empresaRepository.save(empresa)).thenReturn(empresa);
            Empresa r = empresaService.update(empresa);
            assertEquals("Calle Nueva 99", r.getDireccion());
        }

        @Test
        @DisplayName("findAll usa la consulta con tutores")
        void findAllConTutores() {
            when(empresaRepository.findAllWithTutores())
                    .thenReturn(Arrays.asList(empresa));
            List<Empresa> r = empresaService.findAll();
            assertEquals(1, r.size());
            verify(empresaRepository, times(1)).findAllWithTutores();
            verify(empresaRepository, never()).findAll();
        }

        @Test
        @DisplayName("findByNombre devuelve la empresa esperada")
        void findByNombre() {
            when(empresaRepository.findByNombre("Acme S.L.")).thenReturn(empresa);
            assertEquals(1L, empresaService.findByNombre("Acme S.L.").getIdEmpresa());
        }

        @Test
        @DisplayName("delete por id invoca deleteById")
        void deletePorId() {
            empresaService.delete(7L);
            verify(empresaRepository).deleteById(7L);
        }

        @Test
        @DisplayName("deleteInBatch elimina varias empresas")
        void deleteInBatch() {
            List<Empresa> lote = Arrays.asList(empresa, new Empresa());
            empresaService.deleteInBatch(lote);
            verify(empresaRepository).deleteAll(lote);
        }
    }

    // =============================== CURSO ===============================

    @Nested
    @DisplayName("CursoService")
    class CursoTests {

        private Curso curso;

        @BeforeEach
        void init() {
            curso = new Curso();
            curso.setIdCurso(5L);
            curso.setNombre("DAM 2");
            curso.setAnio(2);
        }

        @Test
        @DisplayName("save persiste el curso")
        void save() {
            when(cursoRepository.save(curso)).thenReturn(curso);
            Curso r = cursoService.save(curso);
            assertEquals("DAM 2", r.getNombre());
            assertEquals(2, r.getAnio());
        }

        @Test
        @DisplayName("find devuelve el curso por id")
        void find() {
            when(cursoRepository.findById(5L)).thenReturn(Optional.of(curso));
            Curso r = cursoService.find(5L);
            assertNotNull(r);
            assertEquals(5L, r.getIdCurso());
        }

        @Test
        @DisplayName("find devuelve null cuando no hay resultados")
        void findInexistente() {
            when(cursoRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertNull(cursoService.find(999L));
        }

        @Test
        @DisplayName("findByNombre usa el repositorio")
        void findByNombre() {
            when(cursoRepository.findByNombre("DAM 2")).thenReturn(curso);
            assertEquals(5L, cursoService.findByNombre("DAM 2").getIdCurso());
        }

        @Test
        @DisplayName("delete por entidad invoca delete del repositorio")
        void deleteEntidad() {
            cursoService.delete(curso);
            verify(cursoRepository).delete(curso);
        }
    }

    // ========================== CICLO FORMATIVO ==========================

    @Nested
    @DisplayName("CicloFormativoService")
    class CicloTests {

        private CicloFormativo ciclo;

        @BeforeEach
        void init() {
            ciclo = new CicloFormativo();
            ciclo.setIdCiclo(3L);
            ciclo.setNombre("DAM");
            ciclo.setDescripcion("Desarrollo de Aplicaciones Multiplataforma");
        }

        @Test
        @DisplayName("save guarda el ciclo")
        void save() {
            when(cicloFormativoRepository.save(ciclo)).thenReturn(ciclo);
            CicloFormativo r = cicloFormativoService.save(ciclo);
            assertEquals("DAM", r.getNombre());
        }

        @Test
        @DisplayName("findByNombre devuelve el ciclo correcto")
        void findByNombre() {
            when(cicloFormativoRepository.findByNombre("DAM")).thenReturn(ciclo);
            CicloFormativo r = cicloFormativoService.findByNombre("DAM");
            assertEquals(3L, r.getIdCiclo());
            assertEquals("Desarrollo de Aplicaciones Multiplataforma", r.getDescripcion());
        }

        @Test
        @DisplayName("delete por id delega en deleteById")
        void deletePorId() {
            cicloFormativoService.delete(3L);
            verify(cicloFormativoRepository).deleteById(3L);
        }

        @Test
        @DisplayName("deleteInBatch elimina varios ciclos")
        void deleteInBatch() {
            List<CicloFormativo> lote = Arrays.asList(ciclo);
            cicloFormativoService.deleteInBatch(lote);
            verify(cicloFormativoRepository).deleteAll(lote);
        }
    }

    // ========================== ESTUDIANTE EXTRA =========================

    @Nested
    @DisplayName("EstudianteService (operaciones extra)")
    class EstudianteExtraTests {

        @Test
        @DisplayName("findAll devuelve todos los estudiantes")
        void findAll() {
            Estudiante e1 = new Estudiante();
            e1.setId(1L);
            Estudiante e2 = new Estudiante();
            e2.setId(2L);
            when(estudianteRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

            List<Estudiante> r = estudianteService.findAll();

            assertEquals(2, r.size());
        }

        @Test
        @DisplayName("findByCurso filtra por id de curso")
        void findByCurso() {
            Estudiante e1 = new Estudiante();
            e1.setId(11L);
            when(estudianteRepository.findByCursoIdCurso(5L))
                    .thenReturn(Arrays.asList(e1));

            List<Estudiante> r = estudianteService.findByCurso(5L);

            assertEquals(1, r.size());
            assertEquals(11L, r.get(0).getId());
        }

        @Test
        @DisplayName("update usa save del repositorio")
        void update() {
            Estudiante e = new Estudiante();
            e.setId(1L);
            e.setEmail("nuevo@centro.es");
            when(estudianteRepository.save(any(Estudiante.class))).thenReturn(e);

            Estudiante r = estudianteService.update(e);

            assertEquals("nuevo@centro.es", r.getEmail());
            verify(estudianteRepository).save(e);
        }

        @Test
        @DisplayName("delete por entidad invoca delete del repositorio")
        void deleteEntidad() {
            Estudiante e = new Estudiante();
            e.setId(1L);
            estudianteService.delete(e);
            verify(estudianteRepository).delete(e);
        }

        @Test
        @DisplayName("deleteInBatch borra una lista de estudiantes")
        void deleteInBatch() {
            List<Estudiante> lote = Arrays.asList(new Estudiante(), new Estudiante());
            estudianteService.deleteInBatch(lote);
            verify(estudianteRepository).deleteAll(lote);
        }

        @Test
        @DisplayName("findByEmail devuelve null si no existe el usuario")
        void findByEmailInexistente() {
            when(estudianteRepository.findByEmail("nadie@centro.es")).thenReturn(null);
            assertNull(estudianteService.findByEmail("nadie@centro.es"));
        }
    }

    // ====================== FORMACION EMPRESA EXTRA ======================

    @Nested
    @DisplayName("FormacionEmpresaService (operaciones extra)")
    class FormacionExtraTests {

        @Test
        @DisplayName("findAll devuelve todas las formaciones")
        void findAll() {
            when(formacionEmpresaRepository.findAll())
                    .thenReturn(Arrays.asList(new FormacionEmpresa(),
                                              new FormacionEmpresa(),
                                              new FormacionEmpresa()));

            assertEquals(3, formacionEmpresaService.findAll().size());
        }

        @Test
        @DisplayName("findByEstudiante filtra por estudiante")
        void findByEstudiante() {
            FormacionEmpresa fe = new FormacionEmpresa();
            fe.setEstado("EN_CURSO");
            when(formacionEmpresaRepository.findByEstudianteId(10L))
                    .thenReturn(Arrays.asList(fe));

            List<FormacionEmpresa> r = formacionEmpresaService.findByEstudiante(10L);
            assertEquals(1, r.size());
            assertEquals("EN_CURSO", r.get(0).getEstado());
        }

        @Test
        @DisplayName("findByCurso filtra por id de curso")
        void findByCurso() {
            when(formacionEmpresaRepository.findByCursoIdCurso(5L))
                    .thenReturn(Arrays.asList(new FormacionEmpresa()));
            assertEquals(1, formacionEmpresaService.findByCurso(5L).size());
        }

        @Test
        @DisplayName("findByEstado FINALIZADA")
        void findByEstadoFinalizada() {
            FormacionEmpresa fe = new FormacionEmpresa();
            fe.setEstado("FINALIZADA");
            when(formacionEmpresaRepository.findByEstado("FINALIZADA"))
                    .thenReturn(Arrays.asList(fe));

            List<FormacionEmpresa> r = formacionEmpresaService.findByEstado("FINALIZADA");
            assertEquals(1, r.size());
            assertEquals("FINALIZADA", r.get(0).getEstado());
        }

        @Test
        @DisplayName("findByEstado devuelve lista vacía si no hay coincidencias")
        void findByEstadoVacio() {
            when(formacionEmpresaRepository.findByEstado("CANCELADA"))
                    .thenReturn(Collections.emptyList());
            assertTrue(formacionEmpresaService.findByEstado("CANCELADA").isEmpty());
        }

        @Test
        @DisplayName("delete por id invoca deleteById")
        void deletePorId() {
            formacionEmpresaService.delete(42L);
            verify(formacionEmpresaRepository).deleteById(42L);
        }

        @Test
        @DisplayName("update reutiliza save del repositorio")
        void update() {
            FormacionEmpresa fe = new FormacionEmpresa();
            fe.setEstado("EN_CURSO");
            when(formacionEmpresaRepository.save(fe)).thenReturn(fe);
            assertEquals("EN_CURSO", formacionEmpresaService.update(fe).getEstado());
        }
    }

    // =================== Test combinado catálogo -> alumno ===============

    @Test
    @DisplayName("Flujo combinado: ciclo -> curso -> estudiantes matriculados")
    void flujoCatalogo_cicloCursoEstudiantes() {
        CicloFormativo ciclo = new CicloFormativo();
        ciclo.setIdCiclo(3L);
        ciclo.setNombre("DAM");

        Curso curso = new Curso();
        curso.setIdCurso(5L);
        curso.setNombre("DAM 2");
        curso.setCicloFormativo(ciclo);

        Estudiante alumno = new Estudiante();
        alumno.setId(11L);
        alumno.setFirstName("Pedro");

        when(cicloFormativoRepository.findByNombre("DAM")).thenReturn(ciclo);
        when(cursoRepository.findByNombre("DAM 2")).thenReturn(curso);
        when(estudianteRepository.findByCursoIdCurso(5L)).thenReturn(Arrays.asList(alumno));

        CicloFormativo c = cicloFormativoService.findByNombre("DAM");
        Curso cu = cursoService.findByNombre("DAM 2");
        List<Estudiante> alumnos = estudianteService.findByCurso(cu.getIdCurso());

        assertNotNull(c);
        assertEquals(c.getIdCiclo(), cu.getCicloFormativo().getIdCiclo());
        assertEquals(1, alumnos.size());
        assertEquals("Pedro", alumnos.get(0).getFirstName());
        assertFalse(alumnos.isEmpty());
    }
}
