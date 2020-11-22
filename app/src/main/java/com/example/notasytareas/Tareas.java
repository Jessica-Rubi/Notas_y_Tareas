package com.example.notasytareas;

import java.io.Serializable;

public class Tareas implements Serializable {
    private String id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private String fordate;
    private String completado;

    public Tareas(){};

    public Tareas(String id, String titulo, String descripcion, String fecha, String hora, String fordate, String completado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.fordate = fordate;
        this.completado = completado;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFordate() {
        return fordate;
    }

    public void setFordate(String fordate) {
        this.fordate = fordate;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getCompletado() {
        return completado;
    }

    public void setCompletado(String completado) {
        this.completado = completado;
    }

    @Override
    public String toString() {
        return "\nTitulo: " + this.titulo + "\nDescripcion: " + this.descripcion+ "\nFecha:" + this.fecha+ "\nHora:" + this.hora + "\n" + this.completado;
    }
}
