package es.leerconmonclick.util;

import java.util.ArrayList;

public class User {

    private String email;
    private String nombre;
    private String icono;
    private String tamanio;
    private String daltonismo;
    private ArrayList<ArrayList<String>> notas;
    private ArrayList<String> settings;

    public User(String email, String nombre, ArrayList<String> settings, ArrayList<ArrayList<String>> notas) {
        this.email = email;
        this.nombre = nombre;
        this.settings = settings;
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

    public ArrayList<ArrayList<String>> getNotas() {
        return notas;
    }

    public void setNotas(ArrayList<ArrayList<String>> notas) {
        this.notas = notas;
    }

    public ArrayList<String> getSett() {
        return settings;
    }

    public void setSett(ArrayList<String> sett) {
        this.settings = sett;
    }
}
