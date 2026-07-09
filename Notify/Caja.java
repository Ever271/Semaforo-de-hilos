public class Caja extends Thread {

    private Tienda tienda;
    private int numeroCaja;
    private boolean abierta = true;
    private int clientesAtendidos = 0;
    private String estado = "Ejecutándose";

    public Caja(String nombre, Tienda tienda, int numeroCaja) {
        super(nombre);
        this.tienda = tienda;
        this.numeroCaja = numeroCaja;
    }

    @Override
    public void run() {
        while (abierta) {
            int cliente = tienda.tomarCliente();

            if (cliente == -1) {
                abierta = false;
                estado = "Terminado";
                System.out.println(getName() + " | Estado: " + estado);
            } else if (puedeAtender(cliente)) {
                atenderCliente(cliente);
            }
        }
    }

    private boolean puedeAtender(int cliente) {
        if (numeroCaja == 1) {
            return true;
        } else if (numeroCaja == 2) {
            return cliente % 2 == 0;
        } else if (numeroCaja == 3) {
            return cliente % 2 != 0 && cliente % 3 != 0;
        } else if (numeroCaja == 4) {
            return cliente % 3 == 0;
        } else {
            return true;
        }
    }

    private synchronized void atenderCliente(int cliente) {
        clientesAtendidos++;

        estado = "Ejecutándose";
        System.out.println(getName()
            + " atendiendo al cliente " + cliente
            + " | Prioridad: " + getPriority()
            + " | Estado: " + estado
            + " | Abierta: " + abierta);

        try {
            estado = "Esperando";
            System.out.println(getName() + " | Estado: " + estado + " 500 ms");
            wait(500);

            if (numeroCaja == 1 && clientesAtendidos == 6) {
                estado = "Esperando";
                System.out.println(getName() + " atendió 6 clientes | Estado: " + estado + " 800 ms");
                wait(800);
                clientesAtendidos = 0;
            }

            if (numeroCaja == 5 && clientesAtendidos == 23) {
                abierta = false;
                estado = "Terminado";
                System.out.println(getName() + " atendió 23 clientes | Estado: " + estado);
            }

        } catch (InterruptedException e) {
            estado = "Terminado";
            System.out.println(getName() + " fue interrumpida | Estado: " + estado);
        }

        notify();
    }
}