package best.Aveiro.EBEC.Objects;

import java.util.ArrayList;

public class Team {
    private String name, modality;
    private int credits;
    private ArrayList<String> members;

    private ArrayList<Prova> provas;

    public ArrayList<Prova> getProvas() {
        return provas;
    }

    public void setProvas(ArrayList<Prova> provas) {
        this.provas = provas;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
    public Team(String name){this.name = name;};
    public Team(){};
}
