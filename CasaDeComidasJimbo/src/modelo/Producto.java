
package modelo;

import java.util.Locale;

// Clase abstracta que ofrece un producto ofrecido poor Casa de Comidas Jimbo

public abstract class Producto implements Buscable {

    // atributos

    /*codigo y nombre tienen final ya que despues de
    incializarlos no se les podra asignar o cambiar el valor. */

    private final String codigo;
    private final String nombre;
    private double precio;
    private int stock;


    // Constructor de un producto.
    // es protected para que solo lo puedan usar las clases hijas Plato y Bebida

    protected Producto(String codigo, String nombre, double precio, int stock) {

        // validar que el codigo y el nombre no sean nulos o que no sean un espacio en blanco

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        // validar que el precio sea mayor a 0, que no sea un numero infinito y que el stock sea mayor a 0

        if (precio <= 0 || !Double.isFinite(precio) || stock < 0) {
            throw new IllegalArgumentException("Datos del producto inválidos.");
        }

        // trim elimina espacios en blanco al principio y al final

        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.precio = precio;
        this.stock = stock;
    }

    // metodo para diferenciar entre objetos de tipo Plato y Bebida

    public abstract String getTipoProducto();


    // Actualiza el precio del producto.

    public boolean actualizarPrecio(double nuevoPrecio) {

        // validar que el precio sea mayor a 0 y que no sea un numero infinito
        if (nuevoPrecio <= 0 || !Double.isFinite(nuevoPrecio)) {
            return false;
        }

        // si el precio es valido, se cambia el valor y devuelve true
        this.precio = nuevoPrecio;
        return true;
    }

    //Incorpora unidades al stock.

    public boolean reponerStock(int cantidad) {

        /* validar que la cantidad sea mayor a 0 y que no supere el numero permitido
        por la variable int de Java */

        if (cantidad <= 0 || stock > Integer.MAX_VALUE - cantidad) {
            return false;
        }

        this.stock += cantidad;
        return true;
    }

    // Descuenta unidades del stock

    public boolean descontarStock(int cantidad) {

        if (cantidad <= 0 || cantidad > stock) {
            return false;
        }

        this.stock -= cantidad;
        return true;
    }

    // Busca coincidencias por código o nombre. Implementacion de la interface Buscable

    @Override
    public boolean coincideCon(String criterio) {

        // validar que el criterio no sea nulo y no sea un espacio un blanco
        if (criterio == null || criterio.isBlank()) {
            return false;
        }

        /* si es valido, guarda el valor en bsuqueda eliminando espacios en blancos al principio
        y cambiando mayusculas por minusculas. */

        String busqueda = criterio.trim().toLowerCase(Locale.ROOT);

        // la implementacion sirve tanto para buscar un producto por codigo o por su nombre

        return codigo.toLowerCase(Locale.ROOT).contains(busqueda) || nombre.toLowerCase(Locale.ROOT).contains(busqueda);
    }

    public String getCodigo() {
        return codigo; // devuelve el codigo del producto
    }

    public String getNombre() {
        return nombre; // devuelve el nombre del producto
    }

    public double getPrecio() {
        return precio; // devuelve el precio actual del producto
    }

    public int getStock() {
        return stock; // devuelve la cantidad disponible de stock
    }

    // toString nos sirve para obtener una representacion en texto de un objeto

    @Override
    public String toString() {
        return "Código: " + codigo
                + "\nNombre: " + nombre
                + "\nTipo: " + getTipoProducto()
                + "\nPrecio: $" + precio
                + "\nStock: " + stock;
    }
}
