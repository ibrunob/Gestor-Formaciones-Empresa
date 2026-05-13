package dev.brunob.ProyectoBase2025.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.brunob.ProyectoBase2025.modelo.CicloFormativo;
import dev.brunob.ProyectoBase2025.modelo.Curso;
import dev.brunob.ProyectoBase2025.modelo.Empresa;
import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.repositorios.CicloFormativoRepository;
import dev.brunob.ProyectoBase2025.repositorios.CursoRepository;
import dev.brunob.ProyectoBase2025.repositorios.EmpresaRepository;
import dev.brunob.ProyectoBase2025.repositorios.TutorRepository;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final CicloFormativoRepository cicloFormativoRepository;
    private final CursoRepository cursoRepository;
    private final EmpresaRepository empresaRepository;
    private final TutorRepository tutorRepository;

    public InitialDataLoader(CicloFormativoRepository cicloFormativoRepository,
            CursoRepository cursoRepository,
            EmpresaRepository empresaRepository,
            TutorRepository tutorRepository) {
        this.cicloFormativoRepository = cicloFormativoRepository;
        this.cursoRepository = cursoRepository;
        this.empresaRepository = empresaRepository;
        this.tutorRepository = tutorRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        CicloFormativo vifc302 = createCicloIfMissing("VIFC302", "Ciclo formativo VIFC302");
        CicloFormativo vifc303 = createCicloIfMissing("VIFC303", "Ciclo formativo VIFC303");

        createCursoIfMissing("1VIFC302", 2025, vifc302);
        createCursoIfMissing("2VIFC302", 2025, vifc302);
        createCursoIfMissing("1VIFC303", 2025, vifc303);
        createCursoIfMissing("2VIFC303", 2025, vifc303);

        Empresa asturCode = createEmpresaIfMissing("AsturCode Solutions S.L.", "Calle Uría 18, Oviedo");
        Empresa norteData = createEmpresaIfMissing("NorteData Innovación S.A.", "Parque Tecnológico 7, Gijón",
            "NorteData Innovacion S.A.");

        createTutorIfMissing("laura.tutor", "Laura", "Menéndez Álvarez", "Femenino", "laura123", "684111222", asturCode);
        createTutorIfMissing("diego.tutor", "Diego", "Suárez Blanco", "Masculino", "diego123", "684333444", norteData);
    }

    private CicloFormativo createCicloIfMissing(String nombre, String descripcion) {
        CicloFormativo ciclo = cicloFormativoRepository.findByNombre(nombre);
        if (ciclo != null) {
            return ciclo;
        }

        ciclo = new CicloFormativo();
        ciclo.setNombre(nombre);
        ciclo.setDescripcion(descripcion);
        return cicloFormativoRepository.save(ciclo);
    }

    private Curso createCursoIfMissing(String nombre, int anio, CicloFormativo cicloFormativo) {
        Curso curso = cursoRepository.findByNombre(nombre);
        if (curso != null) {
            return curso;
        }

        curso = new Curso();
        curso.setNombre(nombre);
        curso.setAnio(anio);
        curso.setCicloFormativo(cicloFormativo);
        return cursoRepository.save(curso);
    }

    private Empresa createEmpresaIfMissing(String nombre, String direccion, String... aliases) {
        Empresa empresa = empresaRepository.findByNombre(nombre);
        if (empresa == null) {
            for (String alias : aliases) {
                empresa = empresaRepository.findByNombre(alias);
                if (empresa != null) {
                    break;
                }
            }
        }

        if (empresa == null) {
            empresa = new Empresa();
        }
        empresa.setNombre(nombre);
        empresa.setDireccion(direccion);
        return empresaRepository.save(empresa);
    }

    private Tutor createTutorIfMissing(String email, String firstName, String lastName, String gender,
            String password, String telefono, Empresa empresa) {
        Tutor tutor = tutorRepository.findByEmail(email);
        if (tutor == null) {
            tutor = new Tutor();
            tutor.setEmail(email);
        }

        tutor.setFirstName(firstName);
        tutor.setLastName(lastName);
        tutor.setGender(gender);
        tutor.setDob(LocalDate.of(1988, 1, 1));
        tutor.setPassword(password);
        tutor.setTelefono(telefono);
        tutor.setEmpresa(empresa);
        return tutorRepository.save(tutor);
    }
}