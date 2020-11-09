package com.example.notasytareas;

import java.io.Serializable;

public class Tareas implements Serializable {
    private int id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private String fordate;

    public Tareas(){};

    public Tareas(String titulo, String descripcion, String fecha, String hora, String fordate) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.fordate = fordate;
    }

    public void setId(int id) {
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

    public int getId() {
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

    @Override
    public String toString() {
        return "\nTitulo: " + this.titulo + "\nDescripcion: " + this.descripcion+ "\nFecha:" + this.fecha+ "\nHora:" + this.hora + "\n";
    }
}
