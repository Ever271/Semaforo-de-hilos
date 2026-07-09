import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    }

    private void configurarVentana() {
        setTitle("Simulación de Cruce Vehicular");
        setSize(ANCHO_BASE, ALTO_BASE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(ANCHO_BASE, ALTO_BASE));
    }

    private void configurarToolTips() {
        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(10000);
    }

    private void crearPanelCruce() {
        panelCruce = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarCruce(g);
            }
        };

        panelCruce.setLayout(null);
        panelCruce.setBackground(COLOR_PASTO);

        panelCruce.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensionarElementos();
                panelCruce.repaint();
            }
        });

        setContentPane(panelCruce);
    }

    private void dibujarCruce(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int ancho = panelCruce.getWidth();
        int alto = panelCruce.getHeight();
        int anchoCalle = escalarTamano(ANCHO_CALLE_BASE);

        int xVertical = (ancho - anchoCalle) / 2;
        int yHorizontal = (alto - anchoCalle) / 2;

        int centroX = ancho / 2;
        int centroY = alto / 2;

        dibujarCalles(g2, ancho, alto, anchoCalle, xVertical, yHorizontal);
        dibujarLineasAmarillas(g2, ancho, alto, anchoCalle, xVertical, yHorizontal, centroX, centroY);
        dibujarCrucesPeatonales(g2, anchoCalle, xVertical, yHorizontal);
        dibujarFlechas(g2, centroX, centroY);
    }

    private void dibujarCalles(Graphics2D g2, int ancho, int alto, int anchoCalle, int xVertical, int yHorizontal) {
        g2.setColor(COLOR_CALLE);
        g2.fillRect(xVertical, 0, anchoCalle, alto);
        g2.fillRect(0, yHorizontal, ancho, anchoCalle);
    }

    private void dibujarLineasAmarillas(Graphics2D g2, int ancho, int alto, int anchoCalle,
                                        int xVertical, int yHorizontal, int centroX, int centroY) {

        g2.setColor(COLOR_LINEA_AMARILLA);
        g2.setStroke(new BasicStroke(Math.max(2, escalarTamano(2))));

        int separacionLinea = escalarTamano(4);

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

        int grosorFranja = escalarTamano(16);
        int separacion = escalarTamano(6);
        int cantidadFranjas = 10;
        int largoCruce = escalarTamano(65);

        dibujarCebraHorizontal(g2, xVertical + escalarTamano(6), yHorizontal - largoCruce,
                grosorFranja, largoCruce, separacion, cantidadFranjas);

        dibujarCebraHorizontal(g2, xVertical + escalarTamano(6), yHorizontal + anchoCalle,
                grosorFranja, largoCruce, separacion, cantidadFranjas);

        dibujarCebraVertical(g2, xVertical - largoCruce, yHorizontal + escalarTamano(6),
                largoCruce, grosorFranja, separacion, cantidadFranjas);

        dibujarCebraVertical(g2, xVertical + anchoCalle, yHorizontal + escalarTamano(6),
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
        g2.setStroke(new BasicStroke(Math.max(3, escalarTamano(4))));

        dibujarFlechaArriba(g2, centroX + escalarTamano(95), escalarY(95));
        dibujarFlechaAbajo(g2, centroX - escalarTamano(80), escalarY(545));
        dibujarFlechaIzquierda(g2, escalarX(160), centroY - escalarTamano(70));
        dibujarFlechaDerecha(g2, escalarX(760), centroY + escalarTamano(65));
    }

    private void dibujarFlechaArriba(Graphics2D g2, int x, int y) {
        int largo = escalarTamano(45);
        int punta = escalarTamano(14);
        int baja = escalarTamano(18);

        g2.drawLine(x, y + largo, x, y);
        g2.drawLine(x, y, x - punta, y + baja);
        g2.drawLine(x, y, x + punta, y + baja);
    }

    private void dibujarFlechaAbajo(Graphics2D g2, int x, int y) {
        int largo = escalarTamano(45);
        int punta = escalarTamano(14);
        int sube = escalarTamano(18);

        g2.drawLine(x, y - largo, x, y);
        g2.drawLine(x, y, x - punta, y - sube);
        g2.drawLine(x, y, x + punta, y - sube);
    }

    private void dibujarFlechaIzquierda(Graphics2D g2, int x, int y) {
        int largo = escalarTamano(55);
        int puntaX = escalarTamano(18);
        int puntaY = escalarTamano(14);

        g2.drawLine(x + largo, y, x, y);
        g2.drawLine(x, y, x + puntaX, y - puntaY);
        g2.drawLine(x, y, x + puntaX, y + puntaY);
    }

    private void dibujarFlechaDerecha(Graphics2D g2, int x, int y) {
        int largo = escalarTamano(55);
        int puntaX = escalarTamano(18);
        int puntaY = escalarTamano(14);

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
        int anchoSemaforo = escalarTamano(35);
        int altoSemaforo = escalarTamano(95);

        semaforoNorte.setPosicion(
                escalarX(500),
                escalarY(70),
                anchoSemaforo,
                altoSemaforo
        );

        semaforoSur.setPosicion(
                escalarX(370),
                escalarY(445),
                anchoSemaforo,
                altoSemaforo
        );

        semaforoEste.setPosicion(
                escalarX(760),
                escalarY(310),
                anchoSemaforo,
                altoSemaforo
        );

        semaforoOeste.setPosicion(
                escalarX(95),
                escalarY(210),
                anchoSemaforo,
                altoSemaforo
        );
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
        int yCercaCruce = 435;
        int separacion = 60;

        for (int i = 0; i < 3; i++) {
            autosSur[i] = crearAuto(colorPorIndice(i), VAuto.SUR, x,
                    yCercaCruce + (i * separacion), 35, 55);
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
        auto.setPosicion(
                escalarX(x),
                escalarY(y),
                escalarTamano(ancho),
                escalarTamano(alto)
        );
        agregarEventoAuto(auto);
        return auto;
    }

    private void redimensionarElementos() {
        if (autosNorte == null || autosSur == null || autosEste == null || autosOeste == null) {
            return;
        }

        posicionarSemaforos();

        redimensionarAuto(autosNorte[0], 360, 120, 35, 55);
        redimensionarAuto(autosNorte[1], 360, 60, 35, 55);
        redimensionarAuto(autosNorte[2], 360, 0, 35, 55);

        redimensionarAuto(autosSur[0], 490, 435, 35, 55);
        redimensionarAuto(autosSur[1], 490, 495, 35, 55);
        redimensionarAuto(autosSur[2], 490, 555, 35, 55);

        redimensionarAuto(autosEste[0], 570, 230, 55, 35);
        redimensionarAuto(autosEste[1], 630, 230, 55, 35);
        redimensionarAuto(autosEste[2], 690, 230, 55, 35);

        redimensionarAuto(autosOeste[0], 255, 350, 55, 35);
        redimensionarAuto(autosOeste[1], 195, 350, 55, 35);
        redimensionarAuto(autosOeste[2], 135, 350, 55, 35);
    }

    private void redimensionarAuto(VAuto auto, int x, int y, int ancho, int alto) {
        if (auto != null) {
            auto.setPosicion(
                    escalarX(x),
                    escalarY(y),
                    escalarTamano(ancho),
                    escalarTamano(alto)
            );
        }
    }

    private int escalarX(int valor) {
        return valor * panelCruce.getWidth() / ANCHO_BASE;
    }

    private int escalarY(int valor) {
        return valor * panelCruce.getHeight() / ALTO_BASE;
    }

    private int escalarTamano(int valor) {
        int escalaX = panelCruce.getWidth() * 100 / ANCHO_BASE;
        int escalaY = panelCruce.getHeight() * 100 / ALTO_BASE;
        int escala = Math.min(escalaX, escalaY);

        return valor * escala / 100;
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

    public VAuto[] getAutosNorte() {
        return autosNorte;
    }

    public VAuto[] getAutosSur() {
        return autosSur;
    }

    public VAuto[] getAutosEste() {
        return autosEste;
    }

    public VAuto[] getAutosOeste() {
        return autosOeste;
    }

    public VSemaforo getSemaforoNorte() {
        return semaforoNorte;
    }

    public VSemaforo getSemaforoSur() {
        return semaforoSur;
    }

    public VSemaforo getSemaforoEste() {
        return semaforoEste;
    }

    public VSemaforo getSemaforoOeste() {
        return semaforoOeste;
    }

    public ControladorCruce getControlador() {
        return controlador;
    }

    public static void main(String[] args) {
        VPrincipal ventana = new VPrincipal();
        ventana.setVisible(true);
        ventana.getControlador().iniciarHilos();
    }
}