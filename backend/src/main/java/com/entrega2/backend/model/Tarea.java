package com.entrega2.backend.model;

public class Tarea {
    private String id;
    private String descripcion;
    private boolean critica;
    private String asignadoA;
    private boolean finalizada;

    public Tarea() {}

    public Tarea(String id, String descripcion, boolean critica, String asignadoA, boolean finalizada) {
        this.id = id;
        this.descripcion = descripcion;
        this.critica = critica;
        this.asignadoA = asignadoA;
        this.finalizada = finalizada;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCritica() {
        return critica;
    }

    public void setCritica(boolean critica) {
        this.critica = critica;
    }

    public String getAsignadoA() {
        return asignadoA;
    }

    public void setAsignadoA(String asignadoA) {
        this.asignadoA = asignadoA;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }
}
