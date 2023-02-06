package es.leerconmonclick.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {

    private String email;
    private String nombre;
    private String icon;
    private String tamanio;
    private String daltonismo;
    private ArrayList<Note> notas;
    private ArrayList<String> settings;
    private String pass;
    private List<Task> taskList;


    public User(String email, String nombre, ArrayList<String> settings, ArrayList<Note> notas,String pass, List<Task> taskList,String icon ) {
        this.email = email;
        this.nombre = nombre;
        this.settings = settings;
        this.notas = notas;
        this.pass = pass;
        this.taskList = taskList;
        this.icon = icon;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public ArrayList<Note> getNotas() {
        return notas;
    }

    public void setNotas(ArrayList<Note> notas) {
        this.notas = notas;
    }

    public ArrayList<String> getSett() {
        return settings;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }


    public void setSett(ArrayList<String> sett) {
        this.settings = sett;
    }
}
