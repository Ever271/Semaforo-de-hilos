package modelo;

import java.util.ArrayList;

public class Semaforo {
    public static final String ROJO = "ROJO";
    public static final String VERDE = "VERDE";
    private String estado;
    private int tiempo;
    private int calle;

    public Semaforo() {

    }

    public String getEstado() {
        return estado;
    }

    public int getTiempo() {
        return tiempo;
    }

    public int getCalle() {
        return calle;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public void setCalle(int calle) {
        this.calle = calle;
    }

    public void crear(ArrayList<Semaforo> lista) {
        lista.add(this);
    }

    public ArrayList<Semaforo> ver(ArrayList<Semaforo> lista) {
        return lista;
    }

}