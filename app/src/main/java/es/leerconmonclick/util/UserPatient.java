package es.leerconmonclick.util;

import java.util.Map;

public class UserPatient {

    private String namePatient, agePatient, emailPatient, password,descriptionPatient, nameProfessional,icon;
    private Map<String,Game> stadistic;

    public UserPatient(String namePatient, String agePatient, String emailPatient, String password, String descriptionPatient, String nameProfessional,String icon , Map<String,Game> stadistic) {
        this.namePatient = namePatient;
        this.agePatient = agePatient;
        this.emailPatient = emailPatient;
        this.password = password;
        this.descriptionPatient = descriptionPatient;
        this.nameProfessional = nameProfessional;
        this.icon = icon;
        this.stadistic = stadistic;
    }

    public Map<String, Game> getStadistic() {
        return stadistic;
    }

    public void setStadistics(Map<String, Game> stadistic) {
        this.stadistic = stadistic;
    }

    public String getNameProfessional() {
        return nameProfessional;
    }

    public void setNameProfessional(String nameProfessional) {
        this.nameProfessional = nameProfessional;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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


}
