public class Main {
    public static void main(String[] args) {

        Tienda tienda = new Tienda(180);

        Caja caja1 = new Caja("Caja 1", tienda, 1);
        Caja caja2 = new Caja("Caja 2", tienda, 2);
        Caja caja3 = new Caja("Caja 3", tienda, 3);
        Caja caja4 = new Caja("Caja 4", tienda, 4);
        Caja caja5 = new Caja("Caja 5", tienda, 5);

        caja1.setPriority(5);
        caja2.setPriority(5);
        caja3.setPriority(5);
        caja4.setPriority(5);
        caja5.setPriority(5);

        caja1.start();
        caja2.start();
        caja3.start();
        caja4.start();
        caja5.start();
    }
}