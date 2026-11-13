
import consola.Consola;
import gestion.CasaDeComidasJimbo;
import modelo.Cliente;
import modelo.Plato;
import modelo.Bebida;
import modelo.PedidoDelivery;
import modelo.PedidoRetiro;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        // Creamos el sistema
        CasaDeComidasJimbo sistema = new CasaDeComidasJimbo();

        // Productos de prueba

        Plato polloAsado = new Plato(
                "P03", "Pollo asado", 5500, 10
        );

        Bebida pepsi500 = new Bebida(
                "P56", "Pepsi 500ml.", 2000, 12, "500 ml."
        );

        Plato menuDelDia = new Plato(
                "P13", "Ravioles con bolognesa", 5500, 12,
                Plato.TipoPlato.MENU_ESPECIAL, LocalDate.now()
        );

        Bebida aguaSab1500 = new Bebida(
                "P62", "Agua saborizada 1500ml.", 1500, 6, "1.5 litros."
        );

        sistema.registrarProducto(polloAsado);
        sistema.registrarProducto(pepsi500);
        sistema.registrarProducto(menuDelDia);
        sistema.registrarProducto(aguaSab1500);



        // Clientes de prueba

        Cliente cliente1 = new Cliente(
                "Juan", "Perez", "2615551111", "San Martin 123"
        );

        Cliente cliente2 = new Cliente(
                "Maria", "Gomez", "2615552222", "Belgrano 456"
        );

        sistema.registrarCliente(cliente1);
        sistema.registrarCliente(cliente2);


        // Pedido delivery de prueba

        PedidoDelivery delivery = sistema.crearPedidoDelivery(
                "PED001", cliente1, 1000
        );

        if (delivery != null) {
            sistema.agregarProductoAPedido("PED001", "P03", 1);
            sistema.agregarProductoAPedido("PED001", "P56", 1);
        }


        // Pedido para retirar de prueba

        PedidoRetiro retiro = sistema.crearPedidoRetiro(
                "PED002", cliente2
        );

        if (retiro != null) {
            sistema.agregarProductoAPedido("PED002", "P13", 2);
            sistema.agregarProductoAPedido("PED002", "P62", 1);
        }


        // Iniciar el programa
        Consola consola = new Consola(sistema);
        consola.iniciar();
    }
}
