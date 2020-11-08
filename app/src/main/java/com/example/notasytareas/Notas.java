package com.example.notasytareas;

import java.io.Serializable;

public class Notas implements Serializable {
    private int id;
    private String titulo;
    private String descripcion;
    private String ruta_imagen;

    public Notas() {
    }

    public Notas(int id, String titulo, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Notas(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Notas(int id, String titulo, String descripcion, String ruta_imagen) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ruta_imagen = ruta_imagen;
    }

    public void setRuta_imagen(String ruta_imagen) {
        this.ruta_imagen = ruta_imagen;
    }

    public String getRuta_imagen() {
        return ruta_imagen;
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

    public void setId(int id) {
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
        return "\nID:"+this.id + "\nTitulo:" + this.titulo + "\nDescripcion:" + this.descripcion;
    }
}
