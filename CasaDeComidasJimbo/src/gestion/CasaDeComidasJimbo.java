
package gestion;

import modelo.*;
import java.util.ArrayList;
import java.util.List;

// Administra los productos, clientes y pedidos de Casa de comidas Jimbo.

public class CasaDeComidasJimbo {

    private static final int MAX_MENUS_ESPECIALES_POR_DIA = 2; // con la constante podemos modificar el menu cuando queramos sin buscar el numero en otro lado

    // atributos

    private final List<Producto> productos;
    private final List<Cliente> clientes;
    private final List<Pedido> pedidos;

    // constructor

    public CasaDeComidasJimbo() {
        this.productos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }


    /*
    Registra un producto y controla que no se repita su código.
    Limita a dos los menús especiales por fecha.
     */

    public boolean registrarProducto(Producto producto) {

        if (producto == null || buscarProductoPorCodigo(producto.getCodigo()) != null) {
            return false;
        }

        if (producto instanceof Plato plato && plato.getTipo() == Plato.TipoPlato.MENU_ESPECIAL) {

            int cantidadMenus = 0;

            for (Producto existente : productos) {

                if (existente instanceof Plato platoExistente && platoExistente.getTipo() == Plato.TipoPlato.MENU_ESPECIAL
                        && platoExistente.getFechaDisponible().equals(plato.getFechaDisponible())) {

                    cantidadMenus++;
                }
            }

            if (cantidadMenus >= MAX_MENUS_ESPECIALES_POR_DIA) {
                return false;
            }
        }

        productos.add(producto);
        return true;
    }


    // Registra un cliente evitando teléfonos duplicados.

    public boolean registrarCliente(Cliente cliente) {

        if (cliente == null || buscarClientePorTelefono(cliente.getTelefono()) != null) {
            return false;
        }

        clientes.add(cliente);
        return true;
    }


    public Producto buscarProductoPorCodigo(String codigo) {

        if (codigo == null || codigo.isBlank()) {
            return null;
        }

        for (Producto producto : productos) {
            if (producto.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return producto;
            }
        }

        return null;
    }


    public Cliente buscarClientePorTelefono(String telefono) {

        if (telefono == null || telefono.isBlank()) {
            return null;
        }

        for (Cliente cliente : clientes) {
            if (cliente.getTelefono().equalsIgnoreCase(telefono.trim())) {
                return cliente;
            }
        }

        return null;
    }


    public Pedido buscarPedidoPorCodigo(String codigo) {

        if (codigo == null || codigo.isBlank()) {
            return null;
        }

        for (Pedido pedido : pedidos) {
            if (pedido.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return pedido;
            }
        }

        return null;
    }

    // Comprueba que un código de pedido esté disponible.

    private boolean codigoPedidoDisponible(String codigo) {
        return codigo != null && !codigo.isBlank()
                && buscarPedidoPorCodigo(codigo) == null;
    }

    // Crea un pedido delivery para un cliente registrado.

    public PedidoDelivery crearPedidoDelivery(
            String codigo, Cliente cliente, double costoEnvio) {

        if (!clientes.contains(cliente)
                || !codigoPedidoDisponible(codigo)
                || costoEnvio < 0
                || !Double.isFinite(costoEnvio)) {
            return null;
        }

        PedidoDelivery pedido = new PedidoDelivery(codigo, cliente, costoEnvio);
        pedidos.add(pedido);

        return pedido;
    }

    // Crea un pedido para retirar por el local.

    public PedidoRetiro crearPedidoRetiro(String codigo, Cliente cliente) {

        if (!clientes.contains(cliente) || !codigoPedidoDisponible(codigo)) {
            return null;
        }

        PedidoRetiro pedido = new PedidoRetiro(codigo, cliente);
        pedidos.add(pedido);

        return pedido;
    }


    public boolean agregarProductoAPedido(
            String codigoPedido, String codigoProducto, int cantidad) {

        Pedido pedido = buscarPedidoPorCodigo(codigoPedido);
        Producto producto = buscarProductoPorCodigo(codigoProducto);

        if (pedido == null || producto == null) {
            return false;
        }

        return pedido.agregarProducto(producto, cantidad);
    }


    public boolean modificarCantidadPedido(
            String codigoPedido, String codigoProducto, int cantidad) {

        Pedido pedido = buscarPedidoPorCodigo(codigoPedido);

        if (pedido == null) {
            return false;
        }

        return pedido.modificarCantidad(codigoProducto, cantidad);
    }


    public boolean quitarProductoPedido(
            String codigoPedido, String codigoProducto) {

        Pedido pedido = buscarPedidoPorCodigo(codigoPedido);

        if (pedido == null) {
            return false;
        }

        return pedido.quitarProducto(codigoProducto);
    }


    public boolean actualizarPrecioProducto(String codigo, double nuevoPrecio) {

        Producto producto = buscarProductoPorCodigo(codigo);

        if (producto == null) {
            return false;
        }

        return producto.actualizarPrecio(nuevoPrecio);
    }


    public boolean reponerStockProducto(String codigo, int cantidad) {

        Producto producto = buscarProductoPorCodigo(codigo);

        if (producto == null) {
            return false;
        }

        return producto.reponerStock(cantidad);
    }


    public boolean cambiarEstadoPedido(
            String codigo, Pedido.EstadoPedido nuevoEstado) {

        Pedido pedido = buscarPedidoPorCodigo(codigo);

        if (pedido == null) {
            return false;
        }

        return pedido.cambiarEstado(nuevoEstado);
    }


    public boolean registrarPagoPedido(
            String codigo, Pedido.TipoPago tipoPago) {

        Pedido pedido = buscarPedidoPorCodigo(codigo);

        if (pedido == null) {
            return false;
        }

        return pedido.registrarPago(tipoPago);
    }

    // Busca productos y clientes utilizando la interfaz Buscable.

    public List<Buscable> buscarPorCriterio(String criterio) {

        List<Buscable> resultados = new ArrayList<>();

        for (Producto producto : productos) {
            if (producto.coincideCon(criterio)) {
                resultados.add(producto);
            }
        }

        for (Cliente cliente : clientes) {
            if (cliente.coincideCon(criterio)) {
                resultados.add(cliente);
            }
        }

        return List.copyOf(resultados);
    }

    /*
     Genera un reporte de los pedidos entregados.
     Utiliza polimorfismo para calcular el total de cada pedido.
     */

    public String generarReporteVentas() {

        int cantidadPedidos = 0;
        double totalVentas = 0;

        for (Pedido pedido : pedidos) {

            if (pedido.getEstado() == Pedido.EstadoPedido.ENTREGADO) {
                cantidadPedidos++;
                totalVentas += pedido.calcularTotal();
            }
        }

        return "===== REPORTE DE VENTAS ====="
                + "\nPedidos entregados: " + cantidadPedidos
                + "\nTotal de ventas: $" + totalVentas;
    }

    public List<Producto> getProductos() {
        return List.copyOf(productos);
    } // muestra una lista de productis

    public List<Cliente> getClientes() {
        return List.copyOf(clientes);
    } // muestra una lista de clientes

    public List<Pedido> getPedidos() {
        return List.copyOf(pedidos);
    } // muestra una lista de los pedidos
}
