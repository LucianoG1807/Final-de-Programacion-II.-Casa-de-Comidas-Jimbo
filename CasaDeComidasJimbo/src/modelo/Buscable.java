
package modelo;

// interfaz que implementaran las clases Producto y Cliente, para poder buscar dichos objetos.

public interface Buscable {

    boolean coincideCon(String criterio); // comprueba si el objeto coincide con el criterio de busqueda
}
