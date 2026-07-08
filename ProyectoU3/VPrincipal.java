
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class VPrincipal extends JFrame {

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

    public VPrincipal() {
        setTitle("Simulación de Cruce Vehicular");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        crearPanelCruce();
        crearSemaforos();
        crearAutos();
        controlador = new ControladorCruce(this);
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
        panelCruce.setBackground(new Color(70, 150, 70));
        setContentPane(panelCruce);
    }

    private void dibujarCruce(Graphics g) {
        int ancho = panelCruce.getWidth();
        int alto = panelCruce.getHeight();
        int anchoCalle = 260;
        int xVertical =(ancho - anchoCalle) / 2;
        int yHorizontal =(alto - anchoCalle) / 2;
        g.setColor(new Color(105, 105, 105));
        g.fillRect(xVertical,0,anchoCalle,alto);
        g.fillRect(0,yHorizontal,ancho,anchoCalle);
        g.setColor(Color.WHITE);
        g.drawLine(ancho / 2,0,ancho / 2,yHorizontal);
        g.drawLine(ancho / 2,yHorizontal + anchoCalle,ancho / 2,alto);
        g.drawLine(0,alto / 2,xVertical,alto / 2);
        g.drawLine(xVertical + anchoCalle,alto / 2,ancho,alto / 2);
    }

    private void crearSemaforos() {
        semaforoNorte = new VSemaforo();
        semaforoSur = new VSemaforo();
        semaforoEste = new VSemaforo();
        semaforoOeste = new VSemaforo();
        
        semaforoNorte.setPosicion(470,35,30,70);
        semaforoSur.setPosicion(430,535,30,70);
        semaforoEste.setPosicion(830,270,30,70);
        semaforoOeste.setPosicion(40,330,30,70);
        
        semaforoNorte.setEstado(VSemaforo.VERDE);
        semaforoSur.setEstado(VSemaforo.ROJO);
        semaforoEste.setEstado(VSemaforo.ROJO);
        semaforoOeste.setEstado(VSemaforo.ROJO);

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
            autosNorte[i] = new VAuto();
            autosNorte[i].setColor(colorPorIndice(i));
            autosNorte[i].setDireccion(VAuto.NORTE);
            autosNorte[i].setPosicion(x,yCercaCruce - (i * separacion),35,55);
            panelCruce.add(autosNorte[i]);
        }
    }
    
    private void crearAutosSur() {
        int x = 490;
        int yCercaCruce = 435;
        int separacion = 60;
        for (int i = 0; i < 3; i++) {
            autosSur[i] = new VAuto();
            autosSur[i].setColor(colorPorIndice(i));
            autosSur[i].setDireccion(VAuto.SUR);
            autosSur[i].setPosicion(x,yCercaCruce + (i * separacion),35,55);
            panelCruce.add(autosSur[i]);
        }
    }
    
    
    private void crearAutosEste() {
        int xCercaCruce = 570;
        int y = 230;
        int separacion = 60;
        for (int i = 0; i < 3; i++) {
            autosEste[i] = new VAuto();
            autosEste[i].setColor(colorPorIndice(i));
            autosEste[i].setDireccion(VAuto.ESTE);
            autosEste[i].setPosicion(xCercaCruce + (i * separacion),y,55,35);
            panelCruce.add(autosEste[i]);
        }
    }
    
    private void crearAutosOeste() {
        int xCercaCruce = 255;
        int y = 350;
        int separacion = 60;
        for (int i = 0; i < 3; i++) {
            autosOeste[i] = new VAuto();
            autosOeste[i].setColor(colorPorIndice(i));
            autosOeste[i].setDireccion(VAuto.OESTE);
            autosOeste[i].setPosicion(xCercaCruce - (i * separacion),y,55,35);
            panelCruce.add(autosOeste[i]);
        }
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


