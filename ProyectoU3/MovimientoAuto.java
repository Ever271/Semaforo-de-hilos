 

public class MovimientoAuto implements Runnable {
    private ControladorCruce controlador;
    private int calle;
    private String destino;

    public MovimientoAuto(ControladorCruce controlador,int calle,String destino) {
        this.controlador = controlador;
        this.calle = calle;
        this.destino = destino;
    }

    @Override
    public void run() {
        while (true) {
            controlador.controlarMovimiento(calle, destino);
            dormir();
        }
    }

    private void dormir() {
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
}
