
package modelo;

public class DetallePedido {

    private final Producto producto;
    private int cantidad;
    private final double precioUnitario;

    public DetallePedido(Producto producto, int cantidad) {

        if (producto == null) {
            throw new IllegalArgumentException("El producto es obligatorio.");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
    }

    public double calcularSubtotal() {
        return precioUnitario * cantidad;
    }

    public boolean actualizarCantidad(int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            return false;
        }

        this.cantidad = nuevaCantidad;
        return true;
    }

    public Producto getProducto() {
        return producto; // devuelve el producto del que se pide detalle
    }

    public int getCantidad() {
        return cantidad; // devuelve la cantidad de unidades del producto
    }

    public double getPrecioUnitario() {
        return precioUnitario; // devuelve el precio unitario que se registro
    }

    // Muestra la info del detalle
    @Override
    public String toString() {
        return "| Producto: " + producto.getNombre()
                + " \n| Cantidad: " + cantidad
                + " \n| Precio unitario: $" + precioUnitario
                + " \n| Subtotal: $" + calcularSubtotal();

    }
}
