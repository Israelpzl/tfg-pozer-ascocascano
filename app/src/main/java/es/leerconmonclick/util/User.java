package es.leerconmonclick.util;

import java.util.ArrayList;

public class User {

    private String email;
    private String nombre;
    private String icono;
    private String tamanio;
    private String daltonismo;
    private ArrayList<String> notas;

    public User(String email, String nombre, String icono, String tamanio, String daltonismo, ArrayList<String> notas) {
        this.email = email;
        this.nombre = nombre;
        this.icono = icono;
        this.tamanio = tamanio;
        this.daltonismo = daltonismo;
        this.notas = notas;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public String getDaltonismo() {
        return daltonismo;
    }

    public void setDaltonismo(String daltonismo) {
        this.daltonismo = daltonismo;
    }

    public ArrayList<String> getNotas() {
        return notas;
    }

    public void setNotas(ArrayList<String> notas) {
        this.notas = notas;
    }
}
