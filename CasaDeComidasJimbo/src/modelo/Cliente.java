
package modelo;

import java.util.Locale;

public class Cliente implements Buscable {

    private final String nombre;
    private final String apellido;
    private String telefono;
    private String direccion;

    // Crear un cliente

    public Cliente(String nombre, String apellido,
                   String telefono, String direccion) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }

        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }

        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }

        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.telefono = telefono.trim();
        this.direccion = direccion.trim();
    }


    public boolean actualizarTelefono(String nuevoTelefono) {
        if (nuevoTelefono == null || nuevoTelefono.isBlank()) {
            return false;
        }

        this.telefono = nuevoTelefono.trim();
        return true;
    }


    public boolean actualizarDireccion(String nuevaDireccion) {
        if (nuevaDireccion == null || nuevaDireccion.isBlank()) {
            return false;
        }

        this.direccion = nuevaDireccion.trim();
        return true;
    }

    // Busca coincidencias por nombre, apellido, teléfono o dirección implementando la interface

    public boolean coincideCon(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            return false;
        }

        String busqueda = criterio.trim().toLowerCase(Locale.ROOT);

        return nombre.toLowerCase(Locale.ROOT).contains(busqueda)
                || apellido.toLowerCase(Locale.ROOT).contains(busqueda)
                || telefono.toLowerCase(Locale.ROOT).contains(busqueda)
                || direccion.toLowerCase(Locale.ROOT).contains(busqueda);
    }

    public String getNombre() {
        return nombre;
    } // devuelve el nombre del cliente

    public String getApellido() {
        return apellido;
    } // devuelve el apellido del cliente

    public String getTelefono() {
        return telefono;
    } // devuelve el telefono del cliente

    public String getDireccion() {
        return direccion;
    } // devuelve la direccion del cliente

    // Muestra la info del cliente

    @Override
    public String toString() {
        return "Nombre: " + nombre
                + " | Apellido: " + apellido
                + " | Teléfono: " + telefono
                + " | Dirección: " + direccion;
    }
}
