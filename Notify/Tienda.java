public class Tienda {
    private int totalClientes;
    private int clienteActual = 1;

    public Tienda(int totalClientes) {
        this.totalClientes = totalClientes;
    }

    public synchronized int tomarCliente() {
        if (clienteActual <= totalClientes) {
            return clienteActual++;
        } else {
            return -1;
        }
    }
}