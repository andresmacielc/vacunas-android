package com.vacunas.rest.model;

import java.util.Date;

/**
 * Created by mrcpe on 15/11/2017.
 */

public class Vacuna {

    private Long idVacuna;

    private Long idHijo;

    private String nombreVacuna;

    private Date fechaAplicacion;

    private Boolean aplicada;

    public Long getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(Long idVacuna) {
        this.idVacuna = idVacuna;
    }

    public Long getIdHijo() {
        return idHijo;
    }

    public void setIdHijo(Long idHijo) {
        this.idHijo = idHijo;
    }

    public String getNombreVacuna() {
        return nombreVacuna;
    }

    public void setNombreVacuna(String nombreVacuna) {
        this.nombreVacuna = nombreVacuna;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public Boolean getAplicada() {
        return aplicada;
    }

    public void setAplicada(Boolean aplicada) {
        this.aplicada = aplicada;
    }
}
