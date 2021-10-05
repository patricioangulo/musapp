package com.itzdare.musapp.pojos;

public class Mes {
    private Integer id;
    private String nombre;

    public Mes() {
    }

    public Mes(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
