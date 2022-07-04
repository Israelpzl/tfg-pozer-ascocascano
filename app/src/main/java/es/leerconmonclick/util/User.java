package es.leerconmonclick.util;

public class User {

    private String email;
    private String pass;

    public User (){}

    public User(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
