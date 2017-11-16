package com.vacunas.rest.model;

import java.util.Date;

/**
 * Created by mrcpe on 14/11/2017.
 */

public class Hijo {
    private Long idHijo;

    private Long idPadre;

    private String nombres;

    private String apellidos;

    private Long edad;

    private String documento;

    private Date fechaNacimiento;

    private String sexo;

    public Long getIdHijo() {
        return idHijo;
    }

    public void setIdHijo(Long idHijo) {
        this.idHijo = idHijo;
    }

    public Long getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(Long idPadre) {
        this.idPadre = idPadre;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Long getEdad() {
        return edad;
    }

    public void setEdad(Long edad) {
        edad = edad;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
