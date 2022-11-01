package es.leerconmonclick.util;

import java.util.List;

public class User {


    private String name;
    private String email;
    private String pass;
    private List<Task> taskList;

    public User(String name, String email, String pass, List<Task> taskList) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.taskList = taskList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
