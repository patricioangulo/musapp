package com.itzdare.musapp.pojos;

public class Galeria {

    String descripcion;
    String foto;

    public Galeria() {
    }

    public Galeria(String descripcion, String foto) {
        this.descripcion = descripcion;
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
