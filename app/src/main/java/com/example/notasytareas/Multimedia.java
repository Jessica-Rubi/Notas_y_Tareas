package com.example.notasytareas;

import java.io.Serializable;

public class Multimedia implements Serializable {
    private int id;
    private String titulo;
    private String descripcion;
    private String direccion;
    private String tipo;
    private int idReference;

    public Multimedia() {
    }

    public Multimedia(String titulo, String descripcion, String direccion, String tipo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdReference() {
        return idReference;
    }

    public void setIdReference(int idReference) {
        this.idReference = idReference;
    }

    @Override
    public String toString() {
        return "\nTitulo:" + this.titulo + "\nDescripcion:" + this.descripcion+ "\n";
    }
}
