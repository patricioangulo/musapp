package com.itzdare.musapp.pojos;

public class Edad {
    private Integer id;
    private String edad;

    public Edad() {
    }

    public Edad(Integer id, String edad) {
        this.id = id;
        this.edad = edad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return edad;
    }
}
