package modelo;

import java.util.ArrayList;

public class Auto {
    public static final String ROJO = "ROJO";
    public static final String AZUL = "AZUL";
    public static final String VERDE = "VERDE";
    
    public static final String NORTE = "NORTE";
    public static final String SUR = "SUR";
    public static final String ESTE = "ESTE";
    public static final String OESTE = "OESTE";
    
    public static final String FRENTE = "FRENTE";
    public static final String DERECHA = "DERECHA";
    
    public static final int CALLE_NORTE = 0;
    public static final int CALLE_SUR = 1;
    public static final int CALLE_ESTE = 2;
    public static final int CALLE_OESTE = 3;
        
    private int id;
    private String color;
    private String direccion;
    private String destino;

    private int velocidad;

    private int posicionX;
    private int posicionY;

    private int calle;

    private boolean detenido;

    public Auto() {

    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getColor() {
        return color;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getDestino() {
        return destino;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public int getCalle() {
        return calle;
    }

    public boolean isDetenido() {
        return detenido;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }

    public void setCalle(int calle) {
        this.calle = calle;
    }

    public void setDetenido(boolean detenido) {
        this.detenido = detenido;
    }

    public void crear(ArrayList<Auto> lista) {

        lista.add(this);

    }

    public ArrayList<Auto> ver(ArrayList<Auto> lista) {
        return lista;
    }

}