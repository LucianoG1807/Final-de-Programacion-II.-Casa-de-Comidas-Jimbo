
package modelo;

import java.time.LocalDate; // para utilizar la fecha actual

public class Plato extends Producto {

    // tipos de plato
    public enum TipoPlato {
        HABITUAL,
        MENU_ESPECIAL
    }

    // atributos
    private final TipoPlato tipo;
    private final LocalDate fechaDisponible;

    // Crear un plato habitual.

    public Plato(String codigo, String nombre, double precio, int stock) {
        this(codigo, nombre, precio, stock, TipoPlato.HABITUAL, null);
    }

    // Crear un plato indicando su tipo y fecha de disponibilidad.

    public Plato(String codigo, String nombre, double precio, int stock,
                 TipoPlato tipo, LocalDate fechaDisponible) {

        super(codigo, nombre, precio, stock);

        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de plato es obligatorio.");
        }

        if (tipo == TipoPlato.MENU_ESPECIAL && fechaDisponible == null) {
            throw new IllegalArgumentException("El menú especial necesita una fecha.");
        }

        if (tipo == TipoPlato.HABITUAL && fechaDisponible != null) {
            throw new IllegalArgumentException("El plato habitual no necesita fecha.");
        }

        this.tipo = tipo;
        this.fechaDisponible = fechaDisponible;
    }


    @Override
    public String getTipoProducto() {
        return tipo == TipoPlato.HABITUAL // devuelve el tipo de producto
                ? "Plato habitual"
                : "Menú especial";
    }

    public TipoPlato getTipo() {
        return tipo; // devuelve el tipo de plato
    }

    public LocalDate getFechaDisponible() {
        return fechaDisponible; // devuelve la fecha en la que se sirve el plato
    }


    @Override
    public String toString() {
        return super.toString() // devuelve info del plato
                + (fechaDisponible != null
                ? " | Fecha disponible: " + fechaDisponible : "");
    }

    /*
    como si fueran un if else

    + if
    ? else

     */
}
