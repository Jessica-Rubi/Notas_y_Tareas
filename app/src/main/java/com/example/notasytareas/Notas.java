package com.example.notasytareas;

import java.io.Serializable;

public class Notas implements Serializable {
    private String id;
    private String titulo;
    private String descripcion;

    public Notas() {
    }

    public Notas(String id, String titulo, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "\nTitulo: " + this.titulo + "\nDescripcion: " + this.descripcion + "\n";
    }
}
