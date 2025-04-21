package com.entrega2.backend.model;

public class Usuario {
    private String id;
    private String nombre;
    private String rol;
    private String nivel;
    private boolean disponible;

    public Usuario() {}

    public Usuario(String id, String nombre, String rol, String nivel, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.nivel = nivel;
        this.disponible = disponible;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
