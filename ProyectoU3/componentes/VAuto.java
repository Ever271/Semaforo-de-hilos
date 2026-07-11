package Componentes;

import java.awt.Image;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class VAuto extends JLabel implements Serializable {
    public static final String ROJO = "ROJO";
    public static final String AZUL = "AZUL";
    public static final String VERDE = "VERDE";

    public static final String NORTE = "NORTE";
    public static final String SUR = "SUR";
    public static final String ESTE = "ESTE";
    public static final String OESTE = "OESTE";

    private String color;
    private String direccion;

    private int velocidad;

    private int ancho;
    private int alto;

    private boolean detenido;
    public VAuto() {
        color = ROJO;
        direccion = NORTE;
        velocidad = 2;
        ancho = 60;
        alto = 100;
        detenido = false;
        setOpaque(false);
        setBounds(0, 0, ancho, alto);
        actualizarImagen();
    }
    
    public VAuto(String color, String direccion) {
        this();
        setColor(color);
        setDireccion(direccion);
    }
    
    public String getColor() {
        return color;
    }

    public String getDireccion() {
        return direccion;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public int getPosicionX() {
        return getX();
    }

    public int getPosicionY() {
        return getY();
    }

    public boolean isDetenido() {
        return detenido;
    }

    public void setColor(String color) {
        if (color == null) {
            return;
        }
        this.color = color;
        actualizarImagen();
    }

    public void setDireccion(String direccion) {
        if (direccion == null) {
            return;
        }
        this.direccion = direccion;
        actualizarImagen();
    }

    public void setVelocidad(int velocidad) {

        if (velocidad > 0) {
            this.velocidad = velocidad;
        }

    }

    public void setTamano(int ancho, int alto) {
        if (ancho <= 0 || alto <= 0) {
            return;
        }
        this.ancho = ancho;
        this.alto = alto;
        setSize(ancho, alto);
        actualizarImagen();
    }

    public void setPosicion(int x, int y) {
        setLocation(x, y);
    }

    public void setPosicion(int x, int y, int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        setBounds(x, y, ancho, alto);
        actualizarImagen();
    }

    public void setDetenido(boolean detenido) {
        this.detenido = detenido;
    }
    
    private void actualizarImagen() {
        String archivo = "";
        if (color.equals(ROJO)) {
            archivo = "carroRojo";
        } else if (color.equals(AZUL)) {
            archivo = "carroAzul";
        } else if (color.equals(VERDE)) {
            archivo = "carroVerde";
        }

        if (direccion.equals(NORTE)) {
            archivo += "Norte.png";
        } else if (direccion.equals(SUR)) {
            archivo += "Sur.png";
        } else if (direccion.equals(ESTE)) {
            archivo += "Este.png";

        } else if (direccion.equals(OESTE)) {

            archivo += "Oeste.png";

        }

        try {
        
            java.net.URL ruta = getClass().getResource("/imagenes/" + archivo);
        
            if (ruta == null) {
        
                setText("No existe: " + archivo);
        
                return;
        
            }
        
            ImageIcon icono = new ImageIcon(ruta);
        
            Image imagen = icono.getImage().getScaledInstance(
                    ancho,
                    alto,
                    Image.SCALE_SMOOTH);
        
            setIcon(new ImageIcon(imagen));
        
            setText("");
        
        } catch (Exception e) {
        
            e.printStackTrace();
        
        }

        repaint();

    }
    
    public void moverArriba() {

        if (!detenido) {

            setLocation(getX(), getY() - velocidad);

        }

    }

    public void moverAbajo() {

        if (!detenido) {

            setLocation(getX(), getY() + velocidad);

        }

    }

    public void moverIzquierda() {

        if (!detenido) {

            setLocation(getX() - velocidad, getY());

        }

    }

    public void moverDerecha() {

        if (!detenido) {

            setLocation(getX() + velocidad, getY());

        }

    }

    public void avanzar() {

        if (direccion.equals(NORTE)) {

            moverArriba();

        } else if (direccion.equals(SUR)) {

            moverAbajo();

        } else if (direccion.equals(ESTE)) {

            moverDerecha();

        } else if (direccion.equals(OESTE)) {

            moverIzquierda();

        }

    }

    public void girarDerecha() {

        if (direccion.equals(NORTE)) {

            direccion = ESTE;

        } else if (direccion.equals(ESTE)) {

            direccion = SUR;

        } else if (direccion.equals(SUR)) {

            direccion = OESTE;

        } else {

            direccion = NORTE;

        }

        actualizarImagen();

    }

    public void girarIzquierda() {

        if (direccion.equals(NORTE)) {

            direccion = OESTE;

        } else if (direccion.equals(OESTE)) {

            direccion = SUR;

        } else if (direccion.equals(SUR)) {

            direccion = ESTE;

        } else {

            direccion = NORTE;

        }

        actualizarImagen();

    }

    public void detener() {

        detenido = true;

    }

    public void reanudar() {

        detenido = false;

    }

    public void reiniciar() {

        velocidad = 2;

        detenido = false;

        color = ROJO;

        direccion = NORTE;

        setLocation(0, 0);

        actualizarImagen();

    }

}
