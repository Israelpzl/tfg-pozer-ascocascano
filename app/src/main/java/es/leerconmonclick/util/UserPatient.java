package es.leerconmonclick.util;

public class UserPatient {

    private String namePatient, agePatient, emailPatient, password,descriptionPatient, nameProfessional;

    public UserPatient(String namePatient, String agePatient, String emailPatient, String password, String descriptionPatient, String nameProfessional) {
        this.namePatient = namePatient;
        this.agePatient = agePatient;
        this.emailPatient = emailPatient;
        this.password = password;
        this.descriptionPatient = descriptionPatient;
        this.nameProfessional = nameProfessional;
    }

    public String getNamePatient() {
        return namePatient;
    }

    public void setNamePatient(String namePatient) {
        this.namePatient = namePatient;
    }

    public String getAgePatient() {
        return agePatient;
    }

    public void setAgePatient(String agePatient) {
        this.agePatient = agePatient;
    }

    public String getEmailPatient() {
        return emailPatient;
    }

    public void setEmailPatient(String emailPatient) {
        this.emailPatient = emailPatient;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescriptionPatient() {
        return descriptionPatient;
    }

    public void setDescriptionPatient(String descriptionPatient) {
        this.descriptionPatient = descriptionPatient;
    }

    public String getNameProfessionals() {
        return nameProfessional;
    }

    public void setNameProfessionals(String nameProfessionals) {
        this.nameProfessional = nameProfessionals;
    }
}
