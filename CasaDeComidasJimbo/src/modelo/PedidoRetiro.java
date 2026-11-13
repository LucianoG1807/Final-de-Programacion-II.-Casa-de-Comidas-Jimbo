
package modelo;

public class PedidoRetiro extends Pedido {

    // Crear pedido para retirar

    public PedidoRetiro(String codigo, Cliente cliente) {
        super(codigo, cliente);
    }


    @Override
    public double calcularTotal() {
        return calcularSubtotal();
    }

    // muestra que el pedido se retira por el local

    public String getModalidadRetiro() {
        return "Retiro por el local"; // devuelve como se entrega el pedido
    }

    // muestra la info del pedido
    @Override
    public String toString() {
        return super.toString()
                + " \n| Modalidad: " + getModalidadRetiro()
                + " \n||| TOTAL A PAGAR: $" + calcularTotal();
    }
}
