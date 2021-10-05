package com.itzdare.musapp.pojos;

public class Mascota {

    private String id_user;
    private String id_mascota;
    private String nombre_mascota;
    private String edad_mascota;
    private String descripcion;
    private String foto;
    private String nombre_user;
    private String foto_user;
    private String ciudad;
    private String sexo;
    private String meses;
    private String tipo;
    private String filtro;


    public Mascota() {
    }

    public Mascota(String id_user, String id_mascota, String nombre_mascota, String edad_mascota, String descripcion,
                   String foto, String nombre_user, String foto_user, String ciudad, String sexo, String meses, String tipo,
                   String filtro) {
        this.id_user = id_user;
        this.id_mascota = id_mascota;
        this.nombre_mascota = nombre_mascota;
        this.edad_mascota = edad_mascota;
        this.descripcion = descripcion;
        this.foto = foto;
        this.nombre_user = nombre_user;
        this.foto_user = foto_user;
        this.ciudad = ciudad;
        this.sexo = sexo;
        this.meses = meses;
        this.tipo = tipo;
        this.filtro = filtro;

    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
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

    public String getEdad_mascota() {
        return edad_mascota;
    }

    public void setEdad_mascota(String edad_mascota) {
        this.edad_mascota = edad_mascota;
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

    public String getNombre_user() {
        return nombre_user;
    }

    public void setNombre_user(String nombre_user) {
        this.nombre_user = nombre_user;
    }

    public String getFoto_user() {
        return foto_user;
    }

    public void setFoto_user(String foto_user) {
        this.foto_user = foto_user;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getMeses() {
        return meses;
    }

    public void setMeses(String meses) {
        this.meses = meses;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }
}
