
package modelo;

public class PedidoDelivery extends Pedido {

    private double costoEnvio;

    // crear pedido de delivery

    public PedidoDelivery(String codigo, Cliente cliente, double costoEnvio) {
        super(codigo, cliente);

        if (costoEnvio < 0 || !Double.isFinite(costoEnvio)) {
            throw new IllegalArgumentException("El costo de envío no puede ser negativo.");
        }

        this.costoEnvio = costoEnvio;
    }

    // actualizar costo de envio en caso de que se cobre

    public boolean actualizarCostoEnvio(double nuevoCosto) {

        if (!esModificable() || nuevoCosto < 0 || !Double.isFinite(nuevoCosto)) {
            return false;
        }

        this.costoEnvio = nuevoCosto;
        return true;
    }

    // si el envio se cobra, se calcula el total

    @Override
    public double calcularTotal() {
        return calcularSubtotal() + costoEnvio;
    }

    public double getCostoEnvio() {
        return costoEnvio; // devuelve el costo de envio del pedido
    }

    // Muestra la info del envio

    @Override
    public String toString() {
        return super.toString()
                + " \n| Costo de envío: $" + costoEnvio
                + " \n||| TOTAL A PAGAR: $" + calcularTotal();
    }
}
