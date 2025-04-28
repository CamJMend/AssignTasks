package com.entrega2.backend.model;

public class Usuario {
    private String id;
    private String nombre;
    private String nivel;
    private boolean disponible;
    private int tareasAsignadas;

    public Usuario() {
        this.tareasAsignadas = 0;
    }

    public Usuario(String id, String nombre, String nivel, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
        this.disponible = disponible;
        this.tareasAsignadas = 0;
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
    
    public int getTareasAsignadas() {
        return tareasAsignadas;
    }
    
    public void setTareasAsignadas(int tareasAsignadas) {
        this.tareasAsignadas = tareasAsignadas;
    }
    
    public void incrementarTareas() {
        this.tareasAsignadas++;
    }
    
    public void decrementarTareas() {
        if (this.tareasAsignadas > 0) {
            this.tareasAsignadas--;
        }
    }
}