package Componentes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.JPanel;

public class VSemaforo extends JPanel implements Serializable {
    public static final String ROJO = "ROJO";
    public static final String VERDE = "VERDE";
    public static final String AMARILLO = "AMARILLO";

    private String estado;
    private int ancho;
    private int alto;
    public VSemaforo() {
        estado = ROJO;
        ancho = 40;
        alto = 100;
        
        setOpaque(false);
        setPreferredSize(new Dimension(ancho, alto));
        setSize(ancho, alto);
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        if (!ROJO.equals(estado)&& !AMARILLO.equals(estado)&& !VERDE.equals(estado)) {
            return;
        }
        this.estado = estado;
        repaint();
    }

    public void setPosicion(int x, int y) {
        setLocation(x, y);
    }

    public void setPosicion(int x,int y,int ancho,int alto) {
        this.ancho = ancho;
        this.alto = alto;
        setBounds(x, y, ancho, alto);
        revalidate();
        repaint();
    }

    public void setTamano(int ancho, int alto) {
        if (ancho <= 0 || alto <= 0) {
            return;
        }
        this.ancho = ancho;
        this.alto = alto;
        setPreferredSize(new Dimension(ancho, alto));
        setSize(ancho, alto);
        revalidate();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        int anchoActual = getWidth();
        int altoActual = getHeight();
    
        g.setColor(Color.DARK_GRAY);
        g.fillRoundRect(
            0,
            0,
            anchoActual,
            altoActual,
            10,
            10
        );
    
        int diametro = Math.min(
            anchoActual - 10,
            (altoActual - 20) / 3
        );
    
        int x = (anchoActual - diametro) / 2;
    
        int espacio =
            (altoActual - (diametro * 3)) / 4;
    
        int yRojo = espacio;
    
        int yAmarillo =
            yRojo + diametro + espacio;
    
        int yVerde =
            yAmarillo + diametro + espacio;
    
        if (ROJO.equals(estado)) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GRAY);
        }
    
        g.fillOval(
            x,
            yRojo,
            diametro,
            diametro
        );
    
        if (AMARILLO.equals(estado)) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.GRAY);
        }
    
        g.fillOval(
            x,
            yAmarillo,
            diametro,
            diametro
        );
    
        if (VERDE.equals(estado)) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.GRAY);
        }
    
        g.fillOval(
            x,
            yVerde,
            diametro,
            diametro
        );
    }
}
