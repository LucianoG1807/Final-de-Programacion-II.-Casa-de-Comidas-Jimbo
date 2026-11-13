
package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public abstract class Pedido {

    // estados posibles de un pedido
    // usamos enum, pq es mas factible que usar String, ya que hau menos margen de error

    public enum EstadoPedido {
        RECIBIDO,
        ENTREGADO,
        CANCELADO
    }

    // estados de pago

    public enum EstadoPago {
        PENDIENTE,
        PAGADO
    }

    // tipos de pago

    public enum TipoPago {
        EFECTIVO,
        TRANSFERENCIA
    }

    // atributos

    private final String codigo;
    private final Cliente cliente;
    private final LocalDateTime fechaPedido;
    private final List<DetallePedido> detalles;

    private EstadoPedido estado;
    private EstadoPago estadoPago;
    private TipoPago tipoPago;

    // constructor

    protected Pedido(String codigo, Cliente cliente) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }

        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio.");
        }

        this.codigo = codigo.trim();
        this.cliente = cliente;
        this.fechaPedido = LocalDateTime.now();
        this.detalles = new ArrayList<>();
        this.estado = EstadoPedido.RECIBIDO;
        this.estadoPago = EstadoPago.PENDIENTE;
        this.tipoPago = null;
    }


    public boolean agregarProducto(Producto producto, int cantidad) {

        if (!esModificable() || producto == null || cantidad <= 0) {
            return false;
        }

        for (DetallePedido detalle : detalles) {

            if (detalle.getProducto().getCodigo()
                    .equalsIgnoreCase(producto.getCodigo())) {

                if (cantidad > Integer.MAX_VALUE - detalle.getCantidad()) {
                    return false;
                }

                return detalle.actualizarCantidad(
                        detalle.getCantidad() + cantidad);
            }
        }

        detalles.add(new DetallePedido(producto, cantidad));
        return true;
    }


    public boolean modificarCantidad(String codigoProducto, int cantidad) {

        if (!esModificable() || codigoProducto == null
                || codigoProducto.isBlank() || cantidad <= 0) {
            return false;
        }

        for (DetallePedido detalle : detalles) {

            if (detalle.getProducto().getCodigo()
                    .equalsIgnoreCase(codigoProducto)) {

                return detalle.actualizarCantidad(cantidad);
            }
        }

        return false;
    }


    public boolean quitarProducto(String codigoProducto) {

        if (!esModificable() || codigoProducto == null
                || codigoProducto.isBlank()) {
            return false;
        }

        return detalles.removeIf(detalle ->
                detalle.getProducto().getCodigo()
                        .equalsIgnoreCase(codigoProducto));
    }


    public double calcularSubtotal() {

        double subtotal = 0;

        for (DetallePedido detalle : detalles) {
            subtotal += detalle.calcularSubtotal();
        }

        return subtotal;
    }


    public abstract double calcularTotal();


    public boolean registrarPago(TipoPago tipoPago) {

        if (tipoPago == null || estadoPago == EstadoPago.PAGADO
                || estado == EstadoPedido.CANCELADO || detalles.isEmpty()) {
            return false;
        }

        this.tipoPago = tipoPago;
        this.estadoPago = EstadoPago.PAGADO;

        return true;
    }


    public boolean cambiarEstado(EstadoPedido nuevoEstado) {

        if (nuevoEstado == null || estado == EstadoPedido.ENTREGADO
                || estado == EstadoPedido.CANCELADO) {
            return false;
        }

        if (nuevoEstado == EstadoPedido.CANCELADO) {
            this.estado = EstadoPedido.CANCELADO;
            return true;
        }

        if (nuevoEstado == EstadoPedido.ENTREGADO) {

            if (detalles.isEmpty() || !hayStockDisponible()) {
                return false;
            }

            for (DetallePedido detalle : detalles) {
                detalle.getProducto().descontarStock(
                        detalle.getCantidad());
            }

            this.estado = EstadoPedido.ENTREGADO;
            return true;
        }

        return false;
    }

    // comprobacion de stock para hacer entrega del pedido

    private boolean hayStockDisponible() {

        for (DetallePedido detalle : detalles) {

            if (detalle.getProducto().getStock() < detalle.getCantidad()) {
                return false;
            }
        }

        return true;
    }

    // comprobar si el estado del pedido se puede modificar

    public boolean esModificable() {
        return estado != EstadoPedido.ENTREGADO
                && estado != EstadoPedido.CANCELADO;
    }

    public String getCodigo() {
        return codigo; // devuelve el codigo identificador del pedido
    }

    public Cliente getCliente() {
        return cliente; // devuelve el cliente que hace el pedido
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido; // devuelve fecha y hora del pedido
    }

    public List<DetallePedido> getDetalles() {
        return List.copyOf(detalles); // devuelve una lista de detalles
    }

    public EstadoPedido getEstado() {
        return estado; // devuelve estado actual del pedido
    }

    public EstadoPago getEstadoPago() {
        return estadoPago; // devuelve el estado de pago actual
    }

    public TipoPago getTipoPago() {
        return tipoPago; // devuelve el medio de pago utilizado
    }

    // Muestra informacion general del pedido

    @Override
    public String toString() {
        return "| Pedido: " + codigo
                + " \n| Cliente: " + cliente.getNombre() + " " + cliente.getApellido()
                + " \n| Estado: " + estado
                + " \n| Estado del pago: " + estadoPago
                + " \n| Subtotal: $" + calcularSubtotal();
    }
}
