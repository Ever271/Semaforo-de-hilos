import java.util.ArrayList;
import java.util.Random;
import modelo.Auto;
import modelo.Cruce;
import modelo.Semaforo;
import componentes.VAuto;
import componentes.VSemaforo;
public class ControladorCruce {
    private VPrincipal vista;
    private Cruce cruce;
    private Random random;

    private Thread hiloNorteFrente;
    private Thread hiloNorteDerecha;

    private Thread hiloSurFrente;
    private Thread hiloSurDerecha;

    private Thread hiloEsteFrente;
    private Thread hiloEsteDerecha;

    private Thread hiloOesteFrente;
    private Thread hiloOesteDerecha;

    private int[] autosPasadosTurno = new int[4];

    private final Object controlTurnos = new Object();
    public ControladorCruce(VPrincipal vista) {
        this.vista = vista;
        cruce = new Cruce();
        random = new Random();
        crearSemaforos();
        crearAutos();
    }

    private void crearSemaforos() {
        for (int i = 0; i < 4; i++) {
            Semaforo semaforo = new Semaforo();
            semaforo.setCalle(i);
            semaforo.setTiempo(10);
            if (i == Auto.CALLE_NORTE) {
                semaforo.setEstado(Semaforo.VERDE);
            } else {
                semaforo.setEstado(Semaforo.ROJO);
            }
            semaforo.crear(cruce.getSemaforos());
        }
    }

    private void crearAutos() {
        crearAutosCalle(
            cruce.getNorte(),
            Auto.CALLE_NORTE,
            Auto.SUR);
        crearAutosCalle(
            cruce.getSur(),
            Auto.CALLE_SUR,
            Auto.NORTE);
        crearAutosCalle(
            cruce.getEste(),
            Auto.CALLE_ESTE,
            Auto.OESTE);
        crearAutosCalle(
            cruce.getOeste(),
            Auto.CALLE_OESTE,
            Auto.ESTE);
    }

    private void crearAutosCalle(ArrayList<Auto> lista,int calle,String direccion){
        String[] colores = {
                Auto.ROJO,
                Auto.AZUL,
                Auto.VERDE
            };
        for (int i = 0; i < 3; i++) {
            Auto auto = new Auto();
            auto.setId((calle * 3) + i + 1);
            auto.setColor(colores[i]);
            auto.setDireccion(direccion);
            auto.setDestino(destinoAleatorio());
            auto.setVelocidad(2);
            auto.setCalle(calle);
            auto.setDetenido(true);
            auto.crear(lista);
        }
    }

    private String destinoAleatorio() {
        int numero = random.nextInt(2);
        if (numero == 0) {
            return Auto.FRENTE;
        }
        return Auto.DERECHA;
    }

    private void crearHilos() {
        hiloNorteFrente = new Thread(new MovimientoAuto(this,Auto.CALLE_NORTE,Auto.FRENTE));
        hiloNorteDerecha = new Thread(new MovimientoAuto(this,Auto.CALLE_NORTE,Auto.DERECHA));
        hiloSurFrente = new Thread(new MovimientoAuto(this,Auto.CALLE_SUR,Auto.FRENTE));
        hiloSurDerecha = new Thread(new MovimientoAuto(this,Auto.CALLE_SUR,Auto.DERECHA));
        hiloEsteFrente = new Thread(new MovimientoAuto(this,Auto.CALLE_ESTE,Auto.FRENTE));
        hiloEsteDerecha = new Thread(new MovimientoAuto(this,Auto.CALLE_ESTE,Auto.DERECHA));
        hiloOesteFrente = new Thread(new MovimientoAuto(this,Auto.CALLE_OESTE,Auto.FRENTE));
        hiloOesteDerecha = new Thread(new MovimientoAuto(this,Auto.CALLE_OESTE,Auto.DERECHA));

        hiloNorteFrente.setName("NORTE-FRENTE");
        hiloNorteDerecha.setName("NORTE-DERECHA");

        hiloSurFrente.setName("SUR-FRENTE");
        hiloSurDerecha.setName("SUR-DERECHA");

        hiloEsteFrente.setName("ESTE-FRENTE");
        hiloEsteDerecha.setName("ESTE-DERECHA");

        hiloOesteFrente.setName("OESTE-FRENTE");
        hiloOesteDerecha.setName("OESTE-DERECHA");
    }

    public void iniciarHilos() {
        crearHilos();

        hiloNorteFrente.start();
        hiloNorteDerecha.start();

        hiloSurFrente.start();
        hiloSurDerecha.start();

        hiloEsteFrente.start();
        hiloEsteDerecha.start();

        hiloOesteFrente.start();
        hiloOesteDerecha.start();

        iniciarCicloSemaforos();
    }

    private void iniciarCicloSemaforos() {
        Thread hiloSemaforos = new Thread(new Runnable() {
            @Override
            public void run() {
    
                int calleActual = Auto.CALLE_NORTE;
    
                while (true) {
    
                    cambiarSemaforos(calleActual);
    
                    System.out.println(
                        "\n========== SEMAFORO VERDE: "
                        + nombreCalle(calleActual)
                        + " =========="
                    );
    
                    dormirSemaforo(10000);
    
                    calleActual++;
    
                    if (calleActual > Auto.CALLE_OESTE) {
                        calleActual = Auto.CALLE_NORTE;
                    }
                }
            }
        });

        hiloSemaforos.setName("CONTROL-SEMAFOROS");
        hiloSemaforos.start();
    }

    private String nombreCalle(int calle) {
        switch (calle) {
            case Auto.CALLE_NORTE:
                return "NORTE";

            case Auto.CALLE_SUR:
                return "SUR";

            case Auto.CALLE_ESTE:
                return "ESTE";

            case Auto.CALLE_OESTE:
                return "OESTE";

            default:
                return "DESCONOCIDA";
        }
    }

    private void cambiarSemaforos(int calleVerde) {

        synchronized (controlTurnos) {

            autosPasadosTurno[calleVerde] = 0;

            for (int i = 0; i < cruce.getSemaforos().size(); i++) {

                Semaforo semaforo = cruce.getSemaforos().get(i);

                if (i == calleVerde) {
                    semaforo.setEstado(Semaforo.VERDE);
                } else {
                    semaforo.setEstado(Semaforo.ROJO);
                }
            }
        }

        actualizarSemaforosVisuales();
    }

    private void dormirSemaforo(int tiempo) {

        try {

            Thread.sleep(tiempo);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }

    private void actualizarSemaforosVisuales() {

        vista.getSemaforoSur().setEstado(
            cruce.getSemaforos()
            .get(Auto.CALLE_NORTE)
            .getEstado()
        );

        vista.getSemaforoNorte().setEstado(
            cruce.getSemaforos()
            .get(Auto.CALLE_SUR)
            .getEstado()
        );

        vista.getSemaforoOeste().setEstado(
            cruce.getSemaforos()
            .get(Auto.CALLE_ESTE)
            .getEstado()
        );

        vista.getSemaforoEste().setEstado(
            cruce.getSemaforos()
            .get(Auto.CALLE_OESTE)
            .getEstado()
        );
    }

    private ArrayList<Auto> obtenerListaCalle(int calle) {
        switch (calle) {
            case Auto.CALLE_NORTE:
                return cruce.getNorte();
            case Auto.CALLE_SUR:
                return cruce.getSur();
            case Auto.CALLE_ESTE:
                return cruce.getEste();
            case Auto.CALLE_OESTE:
                return cruce.getOeste();
            default:
                return null;
        }
    }

    public void controlarMovimiento(int calle, String destino) {

        ArrayList<Auto> lista = obtenerListaCalle(calle);

        if (lista == null) {
            return;
        }

        synchronized (lista) {

            if (lista.isEmpty()) {
                return;
            }

            synchronized (controlTurnos) {
                if (autosPasadosTurno[calle] >= 3) {
                    return;
                }
            }

            Auto auto = lista.get(0);

            if (!auto.getDestino().equals(destino)) {
                return;
            }

            if (!Semaforo.VERDE.equals(
                cruce.getSemaforos().get(calle).getEstado())) {
                return;
            }

            if (!auto.isDetenido()) {
                return;
            }

            auto.setDetenido(false);

            VAuto[] autosVisuales = getAutosVista(calle);

            if (autosVisuales == null || autosVisuales.length < 3) {
                auto.setDetenido(true);
                return;
            }

            VAuto autoVisual = autosVisuales[0];

            System.out.println(
                Thread.currentThread().getName()
                + " inicia movimiento | Auto: "
                + auto.getId()
                + " | Destino: "
                + destino
            );
            moverAutoVisual(autoVisual, calle, destino);
            Auto autoQuePaso = lista.remove(0);
            autosVisuales[0] = autosVisuales[1];
            autosVisuales[1] = autosVisuales[2];
            autosVisuales[2] = autoVisual;
            avanzarDosCarros(
                autosVisuales[0],
                autosVisuales[1],
                calle
            );
            colocarAlFinal(
                autosVisuales[2],
                autosVisuales[1],
                calle
            );
            autoQuePaso.setDestino(destinoAleatorio());
            autoQuePaso.setDetenido(true);

            lista.add(autoQuePaso);

            synchronized (controlTurnos) {
                autosPasadosTurno[calle]++;
            }
            System.out.println(
                "AUTOS QUE PASARON EN "
                + nombreCalle(calle)
                + ": "
                + autosPasadosTurno[calle]
                + "/3"
            );
        }
    }

    private void avanzarDosCarros(
    VAuto primero,
    VAuto segundo,
    int calle) {

        int xPrimero = primero.getX();
        int yPrimero = primero.getY();

        int xSegundo = segundo.getX();
        int ySegundo = segundo.getY();

        int separacion = 60;

        switch (calle) {

            case Auto.CALLE_NORTE:

                moverSuave(
                    primero,
                    xPrimero,
                    yPrimero + separacion
                );

                moverSuave(
                    segundo,
                    xSegundo,
                    ySegundo + separacion
                );

                break;

            case Auto.CALLE_SUR:
                moverSuave(
                    primero,
                    xPrimero,
                    yPrimero - separacion
                );

                moverSuave(
                    segundo,
                    xSegundo,
                    ySegundo - separacion
                );

                break;

            case Auto.CALLE_ESTE:
                moverSuave(
                    primero,
                    xPrimero - separacion,
                    yPrimero
                );

                moverSuave(
                    segundo,
                    xSegundo - separacion,
                    ySegundo
                );

                break;

            case Auto.CALLE_OESTE:
                moverSuave(
                    primero,
                    xPrimero + separacion,
                    yPrimero
                );

                moverSuave(
                    segundo,
                    xSegundo + separacion,
                    ySegundo
                );

                break;
        }
    }

    private void colocarAlFinal(
    VAuto autoQuePaso,
    VAuto ultimoAuto,
    int calle) {

        int separacion = 60;

        int destinoX = 0;
        int destinoY = 0;

        switch (calle) {

            case Auto.CALLE_NORTE:

                cambiarOrientacion(
                    autoQuePaso,
                    VAuto.NORTE,
                    30,
                    55
                );

                destinoX = ultimoAuto.getX();
                destinoY = ultimoAuto.getY() - separacion;

                autoQuePaso.setLocation(
                    destinoX,
                    -autoQuePaso.getHeight()
                );

                break;

            case Auto.CALLE_SUR:
                cambiarOrientacion(
                    autoQuePaso,
                    VAuto.SUR,
                    30,
                    55
                );

                destinoX = ultimoAuto.getX();
                destinoY = ultimoAuto.getY() + separacion;

                autoQuePaso.setLocation(
                    destinoX,
                    vista.getHeight()
                );

                break;

            case Auto.CALLE_ESTE:
                cambiarOrientacion(
                    autoQuePaso,
                    VAuto.ESTE,
                    55,
                    30
                );

                destinoX = ultimoAuto.getX() + separacion;
                destinoY = ultimoAuto.getY();

                autoQuePaso.setLocation(
                    vista.getWidth(),
                    destinoY
                );

                break;

            case Auto.CALLE_OESTE:
                cambiarOrientacion(
                    autoQuePaso,
                    VAuto.OESTE,
                    55,
                    30
                );

                destinoX = ultimoAuto.getX() - separacion;
                destinoY = ultimoAuto.getY();

                autoQuePaso.setLocation(
                    -autoQuePaso.getWidth(),
                    destinoY
                );

                break;
        }

        moverSuave(
            autoQuePaso,
            destinoX,
            destinoY
        );
    }

    private void moverAutoVisual(VAuto autoVisual,
    int calle,
    String destino) {

        if (Auto.FRENTE.equals(destino)) {

            moverFrente(autoVisual, calle);

        } else if (Auto.DERECHA.equals(destino)) {

            moverDerecha(autoVisual, calle);
        }
    }

    private void moverFrente(VAuto autoVisual, int calle) {
        int pasos = 900;
        for (int i = 0; i < pasos; i++) {

            switch (calle) {

                case Auto.CALLE_NORTE:
                    autoVisual.setLocation(
                        autoVisual.getX(),
                        autoVisual.getY() + 1
                    );
                    break;

                case Auto.CALLE_SUR:
                    autoVisual.setLocation(
                        autoVisual.getX(),
                        autoVisual.getY() - 1
                    );
                    break;

                case Auto.CALLE_ESTE:
                    autoVisual.setLocation(
                        autoVisual.getX() - 1,
                        autoVisual.getY()
                    );
                    break;

                case Auto.CALLE_OESTE:
                    autoVisual.setLocation(
                        autoVisual.getX() + 1,
                        autoVisual.getY()
                    );
                    break;
            }

            dormirMovimiento(3);
        }
    }

    private void moverDerecha(VAuto autoVisual, int calle) {

        int pasosAntesGiro = 100;
        int pasosDespuesGiro = 800;

        for (int i = 0; i < pasosAntesGiro; i++) {

            switch (calle) {

                case Auto.CALLE_NORTE:
                    autoVisual.setLocation(
                        autoVisual.getX(),
                        autoVisual.getY() + 1
                    );
                    break;

                case Auto.CALLE_SUR:
                    autoVisual.setLocation(
                        autoVisual.getX(),
                        autoVisual.getY() - 1
                    );
                    break;

                case Auto.CALLE_ESTE:
                    autoVisual.setLocation(
                        autoVisual.getX() - 1,
                        autoVisual.getY()
                    );
                    break;

                case Auto.CALLE_OESTE:
                    autoVisual.setLocation(
                        autoVisual.getX() + 1,
                        autoVisual.getY()
                    );
                    break;
            }

            dormirMovimiento(3);
        }

        switch (calle) {
            case Auto.CALLE_NORTE:
                cambiarOrientacion(
                    autoVisual,
                    VAuto.ESTE,
                    55,
                    30
                );
                break;

            case Auto.CALLE_SUR:
                cambiarOrientacion(
                    autoVisual,
                    VAuto.OESTE,
                    55,
                    30
                );
                break;

            case Auto.CALLE_ESTE:
                cambiarOrientacion(
                    autoVisual,
                    VAuto.SUR,
                    30,
                    55
                );
                break;

            case Auto.CALLE_OESTE:
                cambiarOrientacion(
                    autoVisual,
                    VAuto.NORTE,
                    30,
                    55
                );
                break;
        }

        for (int i = 0; i < pasosDespuesGiro; i++) {

            switch (calle) {

                case Auto.CALLE_NORTE:

                    autoVisual.setLocation(
                        autoVisual.getX() - 1,
                        autoVisual.getY()
                    );

                    break;

                case Auto.CALLE_SUR:
                    autoVisual.setLocation(
                        autoVisual.getX() + 1,
                        autoVisual.getY()
                    );

                    break;

                case Auto.CALLE_ESTE:
                    autoVisual.setLocation(
                        autoVisual.getX(),
                        autoVisual.getY() - 1
                    );

                    break;

                case Auto.CALLE_OESTE:
                    autoVisual.setLocation(
                        autoVisual.getX(),
                        autoVisual.getY() + 1
                    );

                    break;
            }

            dormirMovimiento(5);
        }
    }

    private void cambiarOrientacion(
    VAuto auto,
    String direccion,
    int ancho,
    int alto) {

        int centroX = auto.getX() + auto.getWidth() / 2;
        int centroY = auto.getY() + auto.getHeight() / 2;

        auto.setTamano(ancho, alto);

        auto.setDireccion(direccion);

        auto.setLocation(
            centroX - ancho / 2,
            centroY - alto / 2
        );
    }

    private void dormirMovimiento(int tiempo) {

        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void moverSuave(VAuto auto,
    int destinoX,
    int destinoY) {

        while (auto.getX() != destinoX
        || auto.getY() != destinoY) {

            int x = auto.getX();
            int y = auto.getY();

            if (x < destinoX) {
                x++;
            } else if (x > destinoX) {
                x--;
            }

            if (y < destinoY) {
                y++;
            } else if (y > destinoY) {
                y--;
            }

            auto.setLocation(x, y);

            dormirMovimiento(1);
        }
    }

    public VAuto[] getAutosVista(int calle) {
        switch (calle) {

            case Auto.CALLE_NORTE:
                return vista.getAutosNorte();

            case Auto.CALLE_SUR:
                return vista.getAutosSur();

            case Auto.CALLE_ESTE:
                return vista.getAutosEste();

            case Auto.CALLE_OESTE:
                return vista.getAutosOeste();

            default:
                return null;
        }
    }

    public VAuto getAutoVista(int calle, int indice) {
        VAuto[] autos = getAutosVista(calle);
        if (autos == null) {
            return null;
        }
        if (indice < 0 || indice >= autos.length) {
            return null;
        }
        return autos[indice];
    }

    public String obtenerInformacionAuto(VAuto autoVisual) {

        for (int calle = Auto.CALLE_NORTE; calle <= Auto.CALLE_OESTE; calle++) {

            VAuto[] autosVisuales = getAutosVista(calle);
            ArrayList<Auto> listaAutos = obtenerListaCalle(calle);

            if (autosVisuales == null || listaAutos == null) {
                continue;
            }

            for (int i = 0; i < autosVisuales.length && i < listaAutos.size(); i++) {

                if (autosVisuales[i] == autoVisual) {

                    Auto autoModelo = listaAutos.get(i);

                    String hiloAsociado = nombreHilo(calle, autoModelo.getDestino());
                    String hiloEjecutando;

                    if (!autoModelo.isDetenido() && i == 0) {
                        hiloEjecutando = hiloAsociado;
                    } else {
                        hiloEjecutando = "Ninguno en este momento";
                    }

                    String estadoSemaforo = cruce.getSemaforos()
                        .get(calle)
                        .getEstado();
                    return
                    "Características del auto\n\n"
                    + "Color: " + autoModelo.getColor() + "\n"
                    + "Calle de origen: " + nombreCalle(calle) + "\n"
                    + "Orientación visual actual: " + autoVisual.getDireccion() + "\n"
                    + "Velocidad: " + autoModelo.getVelocidad() + "\n"
                    + "Estado del auto: "
                    + (autoModelo.isDetenido() ? "Detenido / esperando" : "En movimiento") + "\n"
                    + "Semáforo de su calle: " + estadoSemaforo + "\n"
                    + "Hilo activo en este momento: " + hiloEjecutando;

                }
            }
        }

        return "No se encontró información para este auto.";
    }

    private String nombreHilo(int calle, String destino) {
        return nombreCalle(calle) + "-" + destino;
    }
}
