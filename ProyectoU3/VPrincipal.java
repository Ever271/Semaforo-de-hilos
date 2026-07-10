import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import componentes.VAuto;
import componentes.VSemaforo;

public class VPrincipal extends JFrame {

    private static final int ANCHO_BASE = 900;
    private static final int ALTO_BASE = 650;
    private static final int ANCHO_CALLE_BASE = 260;

    private JPanel panelCruce;
    private int anchoAnterior = 0;
    private int altoAnterior = 0;
    private VAuto[] autosNorte;
    private VAuto[] autosSur;
    private VAuto[] autosEste;
    private VAuto[] autosOeste;

    private VSemaforo semaforoNorte;
    private VSemaforo semaforoSur;
    private VSemaforo semaforoEste;
    private VSemaforo semaforoOeste;

    private ControladorCruce controlador;

    private final Color COLOR_PASTO = new Color(25, 180, 70);
    private final Color COLOR_CALLE = new Color(55, 55, 55);
    private final Color COLOR_LINEA_AMARILLA = new Color(255, 205, 0);
    private final Color COLOR_LINEA_BLANCA = Color.WHITE;

    public VPrincipal() {
        configurarVentana();
        configurarToolTips();
        crearPanelCruce();
        crearSemaforos();
        crearAutos();
        controlador = new ControladorCruce(this);
        panelCruce.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(
                        java.awt.event.ComponentEvent e) {
        
                    acomodarComponentesAlRedimensionar();
                }
            }
        );
    }

    private void configurarVentana() {
        setTitle("Simulación de Cruce Vehicular");
        setSize(ANCHO_BASE, ALTO_BASE);
        setMinimumSize(new Dimension(ANCHO_BASE, ALTO_BASE));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLayout(new BorderLayout());
    }

    private void configurarToolTips() {
        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(10000);
    }

    private void crearPanelCruce() {
        panelCruce = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarCruce(g);
            }
        };
        panelCruce.setBackground(COLOR_PASTO);
        add(panelCruce, BorderLayout.CENTER);
    }

    private void dibujarCruce(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int ancho = panelCruce.getWidth();
        int alto = panelCruce.getHeight();
    
        int anchoCalle = ANCHO_CALLE_BASE;
    
        int xVertical = (ancho - anchoCalle) / 2;
        int yHorizontal = (alto - anchoCalle) / 2;
    
        int centroX = ancho / 2;
        int centroY = alto / 2;
    
        dibujarCalles(g2,ancho,alto,anchoCalle,xVertical,yHorizontal);
        dibujarLineasAmarillas(g2,ancho,alto,anchoCalle,xVertical,yHorizontal,centroX,centroY);
    
        Graphics2D gComponentes =(Graphics2D) g2.create();
    
        int desplazamientoX =(ancho - ANCHO_BASE) / 2;
        int desplazamientoY =(alto - ALTO_BASE) / 2;
    
        gComponentes.translate(desplazamientoX,desplazamientoY);

        int xVerticalBase =(ANCHO_BASE - ANCHO_CALLE_BASE) / 2;
        int yHorizontalBase =(ALTO_BASE - ANCHO_CALLE_BASE) / 2;
    
        dibujarCrucesPeatonales(gComponentes,ANCHO_CALLE_BASE,xVerticalBase,yHorizontalBase);
        dibujarFlechas(gComponentes,ANCHO_BASE / 2,ALTO_BASE / 2);
    
        gComponentes.dispose();
    }

    private void dibujarCalles(Graphics2D g2, int ancho, int alto, int anchoCalle, int xVertical, int yHorizontal) {
        g2.setColor(COLOR_CALLE);
        g2.fillRect(xVertical, 0, anchoCalle, alto);
        g2.fillRect(0, yHorizontal, ancho, anchoCalle);
    }

    private void dibujarLineasAmarillas(Graphics2D g2, int ancho, int alto, int anchoCalle,
                                        int xVertical, int yHorizontal, int centroX, int centroY) {

        g2.setColor(COLOR_LINEA_AMARILLA);
        g2.setStroke(new BasicStroke(2));

        int separacionLinea = 4;

        g2.drawLine(centroX - separacionLinea, 0, centroX - separacionLinea, yHorizontal);
        g2.drawLine(centroX + separacionLinea, 0, centroX + separacionLinea, yHorizontal);

        g2.drawLine(centroX - separacionLinea, yHorizontal + anchoCalle, centroX - separacionLinea, alto);
        g2.drawLine(centroX + separacionLinea, yHorizontal + anchoCalle, centroX + separacionLinea, alto);

        g2.drawLine(0, centroY - separacionLinea, xVertical, centroY - separacionLinea);
        g2.drawLine(0, centroY + separacionLinea, xVertical, centroY + separacionLinea);

        g2.drawLine(xVertical + anchoCalle, centroY - separacionLinea, ancho, centroY - separacionLinea);
        g2.drawLine(xVertical + anchoCalle, centroY + separacionLinea, ancho, centroY + separacionLinea);
    }

    private void dibujarCrucesPeatonales(Graphics2D g2, int anchoCalle, int xVertical, int yHorizontal) {
        g2.setColor(COLOR_LINEA_BLANCA);

        int grosorFranja = 16;
        int separacion = 6;
        int cantidadFranjas = 10;
        int largoCruce = 65;

        dibujarCebraHorizontal(g2, xVertical + 6, yHorizontal - largoCruce,
                grosorFranja, largoCruce, separacion, cantidadFranjas);

        dibujarCebraHorizontal(g2, xVertical + 6, yHorizontal + anchoCalle,
                grosorFranja, largoCruce, separacion, cantidadFranjas);

        dibujarCebraVertical(g2, xVertical - largoCruce, yHorizontal + 6,
                largoCruce, grosorFranja, separacion, cantidadFranjas);

        dibujarCebraVertical(g2, xVertical + anchoCalle, yHorizontal + 6,
                largoCruce, grosorFranja, separacion, cantidadFranjas);
    }

    private void dibujarCebraHorizontal(Graphics2D g2, int x, int y, int anchoFranja,
                                        int altoFranja, int separacion, int cantidadFranjas) {

        for (int i = 0; i < cantidadFranjas; i++) {
            int posicionX = x + (i * (anchoFranja + separacion));
            g2.fillRect(posicionX, y, anchoFranja, altoFranja);
        }
    }

    private void dibujarCebraVertical(Graphics2D g2, int x, int y, int anchoFranja,
                                      int altoFranja, int separacion, int cantidadFranjas) {

        for (int i = 0; i < cantidadFranjas; i++) {
            int posicionY = y + (i * (altoFranja + separacion));
            g2.fillRect(x, posicionY, anchoFranja, altoFranja);
        }
    }

    private void dibujarFlechas(Graphics2D g2, int centroX, int centroY) {
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));

        dibujarFlechaArriba(g2, centroX + 95, 95);
        dibujarFlechaAbajo(g2, centroX - 80, 545);
        dibujarFlechaIzquierda(g2, 160, centroY - 70);
        dibujarFlechaDerecha(g2, 760, centroY + 65);
    }

    private void dibujarFlechaArriba(Graphics2D g2, int x, int y) {
        int largo = 45;
        int punta = 14;
        int baja = 18;

        g2.drawLine(x, y + largo, x, y);
        g2.drawLine(x, y, x - punta, y + baja);
        g2.drawLine(x, y, x + punta, y + baja);
    }

    private void dibujarFlechaAbajo(Graphics2D g2, int x, int y) {
        int largo = 45;
        int punta = 14;
        int sube = 18;

        g2.drawLine(x, y - largo, x, y);
        g2.drawLine(x, y, x - punta, y - sube);
        g2.drawLine(x, y, x + punta, y - sube);
    }

    private void dibujarFlechaIzquierda(Graphics2D g2, int x, int y) {
        int largo = 55;
        int puntaX = 18;
        int puntaY = 14;

        g2.drawLine(x + largo, y, x, y);
        g2.drawLine(x, y, x + puntaX, y - puntaY);
        g2.drawLine(x, y, x + puntaX, y + puntaY);
    }

    private void dibujarFlechaDerecha(Graphics2D g2, int x, int y) {
        int largo = 55;
        int puntaX = 18;
        int puntaY = 14;

        g2.drawLine(x - largo, y, x, y);
        g2.drawLine(x, y, x - puntaX, y - puntaY);
        g2.drawLine(x, y, x - puntaX, y + puntaY);
    }

    private void crearSemaforos() {
        semaforoNorte = new VSemaforo();
        semaforoSur = new VSemaforo();
        semaforoEste = new VSemaforo();
        semaforoOeste = new VSemaforo();

        posicionarSemaforos();
        configurarEstadosInicialesSemaforos();
        agregarSemaforosAlPanel();
    }

    private void posicionarSemaforos() {
        semaforoNorte.setPosicion(500, 70, 35, 95);
        semaforoSur.setPosicion(370, 445, 35, 95);
        semaforoEste.setPosicion(760, 310, 35, 95);
        semaforoOeste.setPosicion(95, 210, 35, 95);
    }

    private void configurarEstadosInicialesSemaforos() {
        semaforoNorte.setEstado(VSemaforo.VERDE);
        semaforoSur.setEstado(VSemaforo.ROJO);
        semaforoEste.setEstado(VSemaforo.ROJO);
        semaforoOeste.setEstado(VSemaforo.ROJO);
    }

    private void agregarSemaforosAlPanel() {
        panelCruce.add(semaforoNorte);
        panelCruce.add(semaforoSur);
        panelCruce.add(semaforoEste);
        panelCruce.add(semaforoOeste);
    }

    private void crearAutos() {
        autosNorte = new VAuto[3];
        autosSur = new VAuto[3];
        autosEste = new VAuto[3];
        autosOeste = new VAuto[3];

        crearAutosNorte();
        crearAutosSur();
        crearAutosEste();
        crearAutosOeste();
    }

    private void crearAutosNorte() {
        int x = 360;
        int yCercaCruce = 120;
        int separacion = 60;

        for (int i = 0; i < 3; i++) {
            autosNorte[i] = crearAuto(colorPorIndice(i), VAuto.NORTE, x,
                    yCercaCruce - (i * separacion), 35, 55);
            panelCruce.add(autosNorte[i]);
        }
    }

    private void crearAutosSur() {
        int x = 490;
        int yCruceCerca = 435;
        int separacion = 60;

        for (int i = 0; i < 3; i++) {
            autosSur[i] = crearAuto(colorPorIndice(i), VAuto.SUR, x,
                    yCruceCerca + (i * separacion), 35, 55);
            panelCruce.add(autosSur[i]);
        }
    }

    private void crearAutosEste() {
        int xCercaCruce = 570;
        int y = 230;
        int separacion = 60;

        for (int i = 0; i < 3; i++) {
            autosEste[i] = crearAuto(colorPorIndice(i), VAuto.ESTE,
                    xCercaCruce + (i * separacion), y, 55, 35);
            panelCruce.add(autosEste[i]);
        }
    }

    private void crearAutosOeste() {
        int xCercaCruce = 255;
        int y = 350;
        int separacion = 60;

        for (int i = 0; i < 3; i++) {
            autosOeste[i] = crearAuto(colorPorIndice(i), VAuto.OESTE,
                    xCercaCruce - (i * separacion), y, 55, 35);
            panelCruce.add(autosOeste[i]);
        }
    }

    private VAuto crearAuto(String color, String direccion, int x, int y, int ancho, int alto) {
        VAuto auto = new VAuto();
        auto.setColor(color);
        auto.setDireccion(direccion);
        auto.setPosicion(x, y, ancho, alto);
        agregarEventoAuto(auto);
        return auto;
    }

    private String colorPorIndice(int indice) {
        switch (indice) {
            case 0:
                return VAuto.ROJO;
            case 1:
                return VAuto.AZUL;
            default:
                return VAuto.VERDE;
        }
    }

    private void agregarEventoAuto(VAuto auto) {
        auto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mostrarInformacionAuto(auto);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                auto.setToolTipText(null);
            }
        });
    }

    private void mostrarInformacionAuto(VAuto auto) {
        if (controlador != null) {
            String informacion = controlador.obtenerInformacionAuto(auto);
            informacion = informacion.replace("\n", "<br>");
            auto.setToolTipText("<html>" + informacion + "</html>");
        }
    }

    public VAuto[] getAutosNorte() { return autosNorte; }
    public VAuto[] getAutosSur() { return autosSur; }
    public VAuto[] getAutosEste() { return autosEste; }
    public VAuto[] getAutosOeste() { return autosOeste; }
    public VSemaforo getSemaforoNorte() { return semaforoNorte; }
    public VSemaforo getSemaforoSur() { return semaforoSur; }
    public VSemaforo getSemaforoEste() { return semaforoEste; }
    public VSemaforo getSemaforoOeste() { return semaforoOeste; }
    public ControladorCruce getControlador() { return controlador; }

    public static void main(String[] args) {
        VPrincipal ventana = new VPrincipal();
        ventana.setVisible(true);
        ventana.getControlador().iniciarHilos();
    }
    
    private void acomodarComponentesAlRedimensionar() {
        int anchoActual = panelCruce.getWidth();
        int altoActual = panelCruce.getHeight();
        if (anchoAnterior == 0 || altoAnterior == 0) {
            anchoAnterior = anchoActual;
            altoAnterior = altoActual;
            return;
        }
    
        int movimientoX = (anchoActual - anchoAnterior) / 2;
        int movimientoY = (altoActual - altoAnterior) / 2;
    
        if (movimientoX == 0 && movimientoY == 0) {
            return;
        }
    
        moverAutos(autosNorte, movimientoX, movimientoY);
        moverAutos(autosSur, movimientoX, movimientoY);
        moverAutos(autosEste, movimientoX, movimientoY);
        moverAutos(autosOeste, movimientoX, movimientoY);
    
        moverComponente(semaforoNorte, movimientoX, movimientoY);
        moverComponente(semaforoSur, movimientoX, movimientoY);
        moverComponente(semaforoEste, movimientoX, movimientoY);
        moverComponente(semaforoOeste, movimientoX, movimientoY);
    
        anchoAnterior = anchoActual;
        altoAnterior = altoActual;
    }
    private void moverAutos(VAuto[] autos,int movimientoX,int movimientoY) {
        if (autos == null) {
            return;
        }
        for (VAuto auto : autos) {
            if (auto != null) {
                moverComponente(
                    auto,
                    movimientoX,
                    movimientoY
                );
            }
        }
    }
    private void moverComponente(java.awt.Component componente,int movimientoX,int movimientoY) {
        if (componente == null) {
            return;
        }
    
        componente.setLocation(componente.getX() + movimientoX,componente.getY() + movimientoY);
    }
}