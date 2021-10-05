package com.itzdare.musapp.pojos;

public class Solicitudes {
    String estado;
    String idchat;
    String id_mascota;
    String nombre_mascota;
    String foto;
    String token;

    public Solicitudes() {
    }

    public Solicitudes(String estado, String idchat, String id_mascota, String nombre_mascota, String foto, String token) {
        this.estado = estado;
        this.idchat = idchat;
        this.id_mascota = id_mascota;
        this.nombre_mascota = nombre_mascota;
        this.foto = foto;
        this.token = token;

    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdchat() {
        return idchat;
    }

    public void setIdchat(String idchat) {
        this.idchat = idchat;
    }

    public String getId_mascota() {
        return id_mascota;
    }

    public void setId_mascota(String id_mascota) {
        this.id_mascota = id_mascota;
    }

    public String getNombre_mascota() {
        return nombre_mascota;
    }

    public void setNombre_mascota(String nombre_mascota) {
        this.nombre_mascota = nombre_mascota;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}