package dev.brunob.ProyectoBase2025.modelo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad Documento.
 * Representa un documento asociado a una formación en empresa
 * (ej: convenio, programa formativo)
 */
@Entity
@Table(name = "documento")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento", updatable = false, nullable = false)
    private Long idDocumento;

    private String nombre;

    private String tipo;

    @Column(length = 1000)
    private String ruta;

    @Column(name = "fecha_subida")
    private LocalDate fechaSubida;

    @ManyToOne
    @JoinColumn(name = "subido_por")
    private User subidoPor;

    @ManyToOne
    @JoinColumn(name = "formacion_empresa_id")
    private FormacionEmpresa formacionEmpresa;

    public Documento() {
    }

    public Long getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Long idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public LocalDate getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDate fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public User getSubidoPor() {
        return subidoPor;
    }

    public void setSubidoPor(User subidoPor) {
        this.subidoPor = subidoPor;
    }

    public FormacionEmpresa getFormacionEmpresa() {
        return formacionEmpresa;
    }

    public void setFormacionEmpresa(FormacionEmpresa formacionEmpresa) {
        this.formacionEmpresa = formacionEmpresa;
    }

    @Override
    public String toString() {
        return "Documento [idDocumento=" + idDocumento + ", nombre=" + nombre + ", tipo=" + tipo + "]";
    }
}
