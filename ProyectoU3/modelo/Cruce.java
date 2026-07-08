package modelo;

import java.util.ArrayList;

public class Cruce {

    private ArrayList<Auto> norte;
    private ArrayList<Auto> sur;
    private ArrayList<Auto> este;
    private ArrayList<Auto> oeste;

    private ArrayList<Semaforo> semaforos;

    public Cruce() {

        norte = new ArrayList<>();

        sur = new ArrayList<>();

        este = new ArrayList<>();

        oeste = new ArrayList<>();

        semaforos = new ArrayList<>();

    }

    public ArrayList<Auto> getNorte() {
        return norte;
    }

    public ArrayList<Auto> getSur() {
        return sur;
    }

    public ArrayList<Auto> getEste() {
        return este;
    }

    public ArrayList<Auto> getOeste() {
        return oeste;
    }

    public ArrayList<Semaforo> getSemaforos() {
        return semaforos;
    }

    public void setNorte(ArrayList<Auto> norte) {
        this.norte = norte;
    }

    public void setSur(ArrayList<Auto> sur) {
        this.sur = sur;
    }

    public void setEste(ArrayList<Auto> este) {
        this.este = este;
    }

    public void setOeste(ArrayList<Auto> oeste) {
        this.oeste = oeste;
    }

    public void setSemaforos(ArrayList<Semaforo> semaforos) {
        this.semaforos = semaforos;
    }

}