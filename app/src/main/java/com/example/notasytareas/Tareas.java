package com.example.notasytareas;

import java.io.Serializable;

public class Tareas implements Serializable {
    private int id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;

    public Tareas(){};
    public Tareas(String titulo, String descripcion, String fecha, String hora) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
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
        return "\nID:"+this.id + "\nTitulo:" + this.titulo + "\nDescripcion:" + this.descripcion+ "\nFecha:" + this.fecha+ "\nHora:" + this.hora;
    }
}
