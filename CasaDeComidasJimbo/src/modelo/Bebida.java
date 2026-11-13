
package modelo;

public class Bebida extends Producto {

    private final String presentacion;

    // Crear una bebida

    public Bebida(String codigo, String nombre, double precio,
                  int stock, String presentacion) {

        super(codigo, nombre, precio, stock);

        if (presentacion == null || presentacion.isBlank()) {
            throw new IllegalArgumentException("La presentación es obligatoria.");
        }

        this.presentacion = presentacion.trim();
    }


    @Override
    public String getTipoProducto() {
        return "Bebida";
    } // devuelve el tipo de producto, en este caso es bebida

    public String getPresentacion() {
        return presentacion; // devuelve la presentacion de la bebida
    }


    @Override
    public String toString() {
        return super.toString() // devuelve la info de la bebida
                + " | Presentación: " + presentacion;
    }
}
