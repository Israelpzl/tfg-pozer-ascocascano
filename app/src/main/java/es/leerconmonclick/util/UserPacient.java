package es.leerconmonclick.util;

public class UserPacient {

    private String namePacient, agePacient, emailPacient, password,descriptionPacient,nameProfesional;

    public UserPacient(String namePacient, String agePacient, String emailPacient, String password, String descriptionPacient, String nameProfesional) {
        this.namePacient = namePacient;
        this.agePacient = agePacient;
        this.emailPacient = emailPacient;
        this.password = password;
        this.descriptionPacient = descriptionPacient;
        this.nameProfesional = nameProfesional;
    }

    public String getNamePacient() {
        return namePacient;
    }

    public void setNamePacient(String namePacient) {
        this.namePacient = namePacient;
    }

    public String getAgePacient() {
        return agePacient;
    }

    public void setAgePacient(String agePacient) {
        this.agePacient = agePacient;
    }

    public String getEmailPacient() {
        return emailPacient;
    }

    public void setEmailPacient(String emailPacient) {
        this.emailPacient = emailPacient;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescriptionPacient() {
        return descriptionPacient;
    }

    public void setDescriptionPacient(String descriptionPacient) {
        this.descriptionPacient = descriptionPacient;
    }

    public String getNameProfesional() {
        return nameProfesional;
    }

    public void setNameProfesional(String nameProfesional) {
        this.nameProfesional = nameProfesional;
    }
}
