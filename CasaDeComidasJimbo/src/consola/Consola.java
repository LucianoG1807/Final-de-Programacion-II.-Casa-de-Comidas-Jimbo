package consola;

import gestion.CasaDeComidasJimbo;
import modelo.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
La clase consola es la mas importante del proyecto, ya que
es la clase que conectara todas las Clases y subclases del modelo entre si
junto con la clase gestora (CasaDeComidasJimbo)
Tambien se conecta con la clase Main dando asi la interfaz y conectividad
con el usuario.
 */

public class Consola {

    // constante con la opcion para salir del programa.

    private static final int OPCION_SALIR = 14;

    private final CasaDeComidasJimbo sistema;
    private final Scanner teclado;


    public Consola(CasaDeComidasJimbo sistema) {

        if (sistema == null) {
            throw new IllegalArgumentException("El sistema es obligatorio.");
        }

        this.sistema = sistema;
        this.teclado = new Scanner(System.in);
    }

    // Iniciar el menu principal del programa

    public void iniciar() {

        int opcion;

        do {
            mostrarMenu();
            opcion = leerEnteroEnRango(
                    "Seleccioná una opción: ",
                    1, OPCION_SALIR);
            ejecutarOpcion(opcion);

        } while (opcion != OPCION_SALIR);
    }

    // Menu de opciones para ejecutar

    private void ejecutarOpcion(int opcion) {

        try {

            switch (opcion) {
                case 1 -> registrarProducto();
                case 2 -> actualizarPrecio();
                case 3 -> reponerStock();
                case 4 -> registrarCliente();
                case 5 -> crearPedidoDelivery();
                case 6 -> crearPedidoRetiro();
                case 7 -> modificarPedido();
                case 8 -> cambiarEstadoPedido();
                case 9 -> registrarPago();
                case 10 -> listarPedidos();
                case 11 -> buscar();
                case 12 -> consultarStock();
                case 13 -> mostrarReporte();
                case OPCION_SALIR -> System.out.println("Gracias por utilizar Casa de comidas Jimbo.");
            }

        } catch (OperacionCanceladaException e) {
            System.out.println("\nOperación cancelada. Volviendo al menú principal.");
        }
    }

    // Muestra el menu de opciones

    private void mostrarMenu() {

        System.out.println("\n==================================");
        System.out.println("       CASA DE COMIDAS JIMBO");
        System.out.println("==================================");
        System.out.println("1. Registrar producto");
        System.out.println("2. Modificar precio");
        System.out.println("3. Reponer stock");
        System.out.println("4. Registrar cliente");
        System.out.println("5. Crear pedido delivery");
        System.out.println("6. Crear pedido para retirar");
        System.out.println("7. Modificar pedido");
        System.out.println("8. Cambiar estado del pedido");
        System.out.println("9. Registrar pago");
        System.out.println("10. Consultar pedidos");
        System.out.println("11. Buscar clientes y productos");
        System.out.println("12. Consultar stock");
        System.out.println("13. Reporte de ventas");
        System.out.println("14. Salir");
        System.out.println("==================================");
    }

    // Opcion numero 1

    private void registrarProducto() {

        System.out.println("\n--- REGISTRAR PRODUCTO ---");

        String codigo = solicitarCodigoProductoNuevo();
        String nombre = leerTexto("Nombre: ");

        mostrarTiposProducto();

        int opcion = leerEnteroEnRangoConVolver(
                "Seleccioná el tipo (0 para volver): ", 1, 3);

        if (opcion == 0) {
            return;
        }

        double precio;

        do {
            precio = leerDoubleNoNegativo("Precio: $");

            if (precio == 0) {
                System.out.println("El precio debe ser mayor a cero.");
            }

        } while (precio == 0);

        int stock = leerEnteroNoNegativo("Stock inicial: ");

        Producto producto = crearProducto(codigo, nombre, opcion, precio, stock);

        if (producto == null) {
            return;
        }

        if (sistema.registrarProducto(producto)) {
            System.out.println("Producto registrado correctamente.");
        } else {
            System.out.println("No se pudo registrar. Revisá el límite de menús especiales.");
        }
    }


    private void mostrarTiposProducto() {

        System.out.println("1. Plato habitual");
        System.out.println("2. Menú especial");
        System.out.println("3. Bebida");
        System.out.println("0. Volver");
    }


    private Producto crearProducto(
            String codigo, String nombre, int opcion,
            double precio, int stock) {

        // utilizamos try / catch por si las subclases Plato / Bebida largan algun error al crear el producto

        try {

            switch (opcion) {

                case 1:
                    return new Plato(codigo, nombre, precio, stock);

                case 2:
                    LocalDate fecha = leerFecha("Fecha del menú (AAAA-MM-DD): ");

                    return new Plato(
                            codigo, nombre, precio, stock,
                            Plato.TipoPlato.MENU_ESPECIAL, fecha
                    );

                case 3:
                    String presentacion = leerTexto("Presentación: ");

                    return new Bebida(
                            codigo, nombre, precio, stock, presentacion
                    );

                default:
                    return null;
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    private String solicitarCodigoProductoNuevo() {

        while (true) {

            String codigo = leerTexto("Código: ");

            if (sistema.buscarProductoPorCodigo(codigo) == null) {
                return codigo;
            }

            System.out.println("Ese código ya existe. Ingresá otro.");
        }
    }

    // opcion numero 2

    private void actualizarPrecio() {

        System.out.println("\n--- MODIFICAR PRECIO ---");

        Producto producto = solicitarProductoExistente();

        double precio;

        do {
            precio = leerDoubleNoNegativo("Nuevo precio: $");

            if (precio == 0) {
                System.out.println("El precio debe ser mayor a cero.");
            }

        } while (precio == 0);

        if (sistema.actualizarPrecioProducto(producto.getCodigo(), precio)) {
            System.out.println("Precio actualizado correctamente.");
        } else {
            System.out.println("No se pudo actualizar el precio.");
        }
    }

    // Opcion numero 3

    private void reponerStock() {

        System.out.println("\n--- REPONER STOCK ---");

        Producto producto = solicitarProductoExistente();

        while (true) {

            int cantidad = leerEnteroNoNegativo("Cantidad a reponer: ");

            if (cantidad == 0) {
                System.out.println("La cantidad debe ser mayor a cero.");
                continue;
            }

            if (sistema.reponerStockProducto(producto.getCodigo(), cantidad)) {
                System.out.println("Stock actualizado correctamente.");
                return;
            }

            System.out.println("No se pudo reponer. El stock supera el límite permitido.");
        }
    }

    // Opocion numero 4

    private void registrarCliente() {

        System.out.println("\n--- REGISTRAR CLIENTE ---");

        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String telefono = solicitarTelefonoNuevo();
        String direccion = leerTexto("Dirección: ");

        try {

            Cliente cliente = new Cliente(
                    nombre, apellido, telefono, direccion
            );

            if (sistema.registrarCliente(cliente)) {
                System.out.println("Cliente registrado correctamente.");
            } else {
                System.out.println("No se pudo registrar el cliente.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private String solicitarTelefonoNuevo() {

        while (true) {

            String telefono = leerTexto("Teléfono: ");

            if (sistema.buscarClientePorTelefono(telefono) == null) {
                return telefono;
            }

            System.out.println("Ese teléfono ya está registrado. Ingresá otro.");
        }
    }

    // Opcion numero 5

    private void crearPedidoDelivery() {

        System.out.println("\n--- PEDIDO DELIVERY ---");

        Cliente cliente = solicitarClienteExistente();
        String codigo = solicitarCodigoPedidoDisponible();

        double costoEnvio = leerDoubleNoNegativo(
                "Costo de envío (0 si es gratis): $"
        );

        try {

            PedidoDelivery pedido = sistema.crearPedidoDelivery(codigo, cliente, costoEnvio);

            if (pedido == null) {
                System.out.println("No se pudo crear el pedido.");
                return;
            }

            agregarProductosConCancelacion(codigo);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Opcion numero 6

    private void crearPedidoRetiro() {

        System.out.println("\n--- PEDIDO PARA RETIRAR ---");

        Cliente cliente = solicitarClienteExistente();
        String codigo = solicitarCodigoPedidoDisponible();

        PedidoRetiro pedido = sistema.crearPedidoRetiro(
                codigo, cliente
        );

        if (pedido == null) {
            System.out.println("No se pudo crear el pedido.");
            return;
        }

        agregarProductosConCancelacion(codigo);
    }


    private void agregarProductosConCancelacion(String codigoPedido) {

        try {
            agregarProductosAlPedido(codigoPedido);

        } catch (OperacionCanceladaException e) {

            boolean cancelado = sistema.cambiarEstadoPedido(
                    codigoPedido, Pedido.EstadoPedido.CANCELADO);

            if (cancelado) {
                System.out.println("El pedido quedó cancelado para evitar dejarlo incompleto.");
            } else {
                System.out.println("No se pudo cancelar automáticamente el pedido.");
            }

            throw e;
        }
    }


    private void agregarProductosAlPedido(String codigoPedido) {

        System.out.println("\n--- AGREGAR PRODUCTOS ---");
        System.out.println("Escribí FIN para terminar o 0 para cancelar el pedido.");

        while (true) {

            String codigoProducto = leerTexto("Código del producto: ");

            if (codigoProducto.equalsIgnoreCase("FIN")) {

                Pedido pedido = sistema.buscarPedidoPorCodigo(codigoPedido);

                if (pedido != null && !pedido.getDetalles().isEmpty()) {
                    break;
                }

                System.out.println("El pedido debe tener al menos un producto.");
                continue;
            }

            Producto producto = sistema.buscarProductoPorCodigo(codigoProducto);

            if (producto == null) {
                System.out.println("Producto inexistente. Intentá nuevamente.");
                continue;
            }

            int cantidad;

            do {
                cantidad = leerEnteroNoNegativo("Cantidad: ");

                if (cantidad == 0) {
                    System.out.println("La cantidad debe ser mayor a cero.");
                }

            } while (cantidad == 0);

            if (sistema.agregarProductoAPedido(
                    codigoPedido, codigoProducto, cantidad)) {

                System.out.println("Producto agregado correctamente.");

            } else {
                System.out.println("No se pudo agregar el producto.");
            }
        }

        Pedido pedido = sistema.buscarPedidoPorCodigo(codigoPedido);

        if (pedido != null) {
            System.out.println("\nPedido registrado:");
            System.out.println(pedido);
        }
    }

    // Opcion numero 7

    private void modificarPedido() {

        System.out.println("\n--- MODIFICAR PEDIDO ---");

        Pedido pedido = solicitarPedidoExistente();

        if (!pedido.esModificable()) {
            System.out.println("El pedido no puede modificarse.");
            return;
        }

        System.out.println("1. Agregar producto");
        System.out.println("2. Modificar cantidad");
        System.out.println("3. Quitar producto");
        System.out.println("0. Volver");

        int opcion = leerEnteroEnRangoConVolver(
                "Seleccioná una opción (0 para volver): ", 1, 3);

        switch (opcion) {
            case 1 -> agregarProductoPedido(pedido);
            case 2 -> modificarCantidadPedido(pedido);
            case 3 -> quitarProductoPedido(pedido);
        }
    }


    private void agregarProductoPedido(Pedido pedido) {

        Producto producto = solicitarProductoExistente();

        int cantidad;

        do {
            cantidad = leerEnteroNoNegativo("Cantidad: ");

            if (cantidad == 0) {
                System.out.println("La cantidad debe ser mayor a cero.");
            }

        } while (cantidad == 0);

        if (sistema.agregarProductoAPedido(
                pedido.getCodigo(), producto.getCodigo(), cantidad)) {

            System.out.println("Producto agregado correctamente.");
        } else {
            System.out.println("No se pudo agregar el producto.");
        }
    }


    private void modificarCantidadPedido(Pedido pedido) {

        String codigo = solicitarCodigoProductoDelPedido(pedido);

        if (codigo == null) {
            return;
        }

        int cantidad;

        do {
            cantidad = leerEnteroNoNegativo("Nueva cantidad: ");

            if (cantidad == 0) {
                System.out.println("La cantidad debe ser mayor a cero.");
            }

        } while (cantidad == 0);

        if (sistema.modificarCantidadPedido(
                pedido.getCodigo(), codigo, cantidad)) {

            System.out.println("Cantidad modificada correctamente.");
        } else {
            System.out.println("No se pudo modificar la cantidad.");
        }
    }


    private void quitarProductoPedido(Pedido pedido) {

        String codigo = solicitarCodigoProductoDelPedido(pedido);

        if (codigo == null) {
            return;
        }

        if (sistema.quitarProductoPedido(pedido.getCodigo(), codigo)) {
            System.out.println("Producto eliminado del pedido.");
        } else {
            System.out.println("No se pudo quitar el producto.");
        }
    }

    // opcion numero 8

    private void cambiarEstadoPedido() {

        System.out.println("\n--- CAMBIAR ESTADO ---");

        Pedido pedido = solicitarPedidoExistente();

        if (!pedido.esModificable()) {
            System.out.println("El pedido ya está entregado o cancelado.");
            return;
        }

        System.out.println("\nSeleccioná el nuevo estado:");
        System.out.println("1. ENTREGADO");
        System.out.println("2. CANCELADO");
        System.out.println("0. Volver");

        int opcion = leerEnteroEnRango("Opción: ", 0, 2);

        if (opcion == 0) {
            System.out.println("No se realizaron cambios.");
            return;
        }

        Pedido.EstadoPedido nuevoEstado;

        if (opcion == 1) {
            nuevoEstado = Pedido.EstadoPedido.ENTREGADO;
        } else {
            nuevoEstado = Pedido.EstadoPedido.CANCELADO;
        }

        if (sistema.cambiarEstadoPedido(pedido.getCodigo(), nuevoEstado)) {

            System.out.println("Estado actualizado correctamente.");

            if (nuevoEstado == Pedido.EstadoPedido.ENTREGADO) {

                System.out.println("\n¿Deseás registrar el pago?");
                System.out.println("1. Sí");
                System.out.println("2. No");

                int opcionPago = leerEnteroEnRango("Opción: ", 1, 2);

                if (opcionPago == 1) {

                    System.out.println("\nSeleccioná el medio de pago:");
                    System.out.println("1. Efectivo");
                    System.out.println("2. Transferencia");

                    int medioPago = leerEnteroEnRango("Opción: ", 1, 2);

                    Pedido.TipoPago tipoPago = (medioPago == 1)
                            ? Pedido.TipoPago.EFECTIVO
                            : Pedido.TipoPago.TRANSFERENCIA;

                    if (sistema.registrarPagoPedido(pedido.getCodigo(), tipoPago)) {
                        System.out.println("Pago registrado correctamente.");
                    } else {
                        System.out.println("No se pudo registrar el pago.");
                    }
                }
            }

        } else {
            System.out.println("No se pudo cambiar el estado. Verificá que haya stock suficiente y que el pedido sea válido.");
        }
    }


    // Opcion numero 9

    private void registrarPago() {

        System.out.println("\n--- REGISTRAR PAGO ---");

        Pedido pedido = solicitarPedidoExistente();

        if (pedido.getEstado() == Pedido.EstadoPedido.CANCELADO
                || pedido.getEstadoPago() == Pedido.EstadoPago.PAGADO
                || pedido.getDetalles().isEmpty()) {

            System.out.println("El pedido no puede recibir un pago.");
            return;
        }

        System.out.println("1. Efectivo");
        System.out.println("2. Transferencia");
        System.out.println("0. Volver");

        int opcion = leerEnteroEnRangoConVolver(
                "Medio de pago (0 para volver): ", 1, 2);

        if (opcion == 0) {
            return;
        }

        Pedido.TipoPago tipoPago = opcion == 1
                ? Pedido.TipoPago.EFECTIVO
                : Pedido.TipoPago.TRANSFERENCIA;

        if (sistema.registrarPagoPedido(pedido.getCodigo(), tipoPago)) {
            System.out.println("Pago registrado correctamente.");
        } else {
            System.out.println("No se pudo registrar el pago.");
        }
    }

    // Opcion num 10

    private void listarPedidos() {

        System.out.println("\n--- LISTADO DE PEDIDOS ---");

        List<Pedido> pedidos = sistema.getPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }

        for (Pedido pedido : pedidos) {
            mostrarDetallePedido(pedido);
        }
    }


    private void mostrarDetallePedido(Pedido pedido) {

        System.out.println("\n" + pedido);

        System.out.println("Detalles:");
        for (DetallePedido detalle : pedido.getDetalles()) {
            System.out.println(detalle);
        }
    }

    // Busca productos y clientes utilizando la interfaz Buscable.
    // Opcion numero 11

    private void buscar() {

        System.out.println("\n--- BÚSQUEDA ---");

        String criterio = leerTexto(
                "Nombre, apellido, teléfono, dirección o código: "
        );

        List<Buscable> resultados = sistema.buscarPorCriterio(criterio);

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron resultados.");
            return;
        }

        for (Buscable resultado : resultados) {
            System.out.println(resultado);
        }
    }

    //Opcion numero 12

    private void consultarStock() {

        System.out.println("\n--- STOCK DE PRODUCTOS ---");

        List<Producto> productos = sistema.getProductos();

        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (Producto producto : productos) {
            System.out.println(producto.getNombre()
                    + " | Stock: " + producto.getStock());
        }
    }

    // reporte de ventas
    // Opcion num 13

    private void mostrarReporte() {
        System.out.println("\n" + sistema.generarReporteVentas());
    }


    private Producto solicitarProductoExistente() {

        while (true) {

            String codigo = leerTexto("Código del producto: ");
            Producto producto = sistema.buscarProductoPorCodigo(codigo);

            if (producto != null) {
                return producto;
            }

            System.out.println("Producto inexistente. Intentá nuevamente.");
        }
    }


    private Cliente solicitarClienteExistente() {

        List<Cliente> clientes = sistema.getClientes();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados. Primero tenés que registrar un cliente.");
            throw new OperacionCanceladaException();
        }

        while (true) {

            System.out.println("\n--- SELECCIONAR CLIENTE ---");
            System.out.println("1. Ver lista de clientes");
            System.out.println("2. Buscar cliente");
            System.out.println("0. Volver");

            int opcion = leerEnteroEnRangoConVolver(
                    "Seleccioná una opción: ", 1, 2);

            if (opcion == 0) {
                throw new OperacionCanceladaException();
            }

            if (opcion == 1) {
                Cliente seleccionado = seleccionarClienteDeLista(clientes);
                if (seleccionado != null) {
                    return seleccionado;
                }
                continue;
            }

            String criterio = leerTexto("Ingresá nombre, apellido o teléfono: ");
            List<Cliente> resultados = new ArrayList<>();

            for (Cliente cliente : clientes) {
                if (cliente.coincideCon(criterio)) {
                    resultados.add(cliente);
                }
            }

            if (resultados.isEmpty()) {
                System.out.println("No se encontraron clientes con ese criterio.");
                continue;
            }

            Cliente seleccionado = seleccionarClienteDeLista(resultados);
            if (seleccionado != null) {
                return seleccionado;
            }
        }
    }


    private Cliente seleccionarClienteDeLista(List<Cliente> clientes) {

        System.out.println("\n--- CLIENTES DISPONIBLES ---");

        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            System.out.println((i + 1) + ". "
                    + cliente.getNombre() + " " + cliente.getApellido()
                    + " | Teléfono: " + cliente.getTelefono());
        }

        System.out.println("0. Volver");

        int opcion = leerEnteroEnRangoConVolver(
                "Seleccioná un cliente: ", 1, clientes.size());

        if (opcion == 0) {
            return null;
        }

        return clientes.get(opcion - 1);
    }


    private Pedido solicitarPedidoExistente() {

        while (true) {

            String codigo = leerTexto("Código del pedido: ");
            Pedido pedido = sistema.buscarPedidoPorCodigo(codigo);

            if (pedido != null) {
                return pedido;
            }

            System.out.println("Pedido inexistente. Intentá nuevamente.");
        }
    }


    private String solicitarCodigoPedidoDisponible() {

        while (true) {

            String codigo = leerTexto("Código del pedido: ");

            if (sistema.buscarPedidoPorCodigo(codigo) == null) {
                return codigo;
            }

            System.out.println("Ese código de pedido ya existe. Ingresá otro.");
        }
    }


    private String solicitarCodigoProductoDelPedido(Pedido pedido) {

        if (pedido.getDetalles().isEmpty()) {
            System.out.println("El pedido no tiene productos.");
            return null;
        }

        while (true) {

            String codigo = leerTexto("Código del producto: ");

            for (DetallePedido detalle : pedido.getDetalles()) {

                if (detalle.getProducto().getCodigo()
                        .equalsIgnoreCase(codigo)) {

                    return codigo;
                }
            }

            System.out.println("Ese producto no está incluido en el pedido.");
        }
    }


    private int leerEntero(String mensaje) {

        while (true) {

            System.out.print(mensaje);
            String entrada = teclado.nextLine().trim();

            if (entrada.equalsIgnoreCase("VOLVER")) {
                throw new OperacionCanceladaException();
            }

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Ingresá un número entero válido.");
            }
        }
    }


    private int leerEnteroEnRango(String mensaje, int minimo, int maximo) {

        while (true) {

            int valor = leerEntero(mensaje);

            if (valor >= minimo && valor <= maximo) {
                return valor;
            }

            System.out.println("Ingresá una opción entre "
                    + minimo + " y " + maximo + ".");
        }
    }


    private int leerEnteroEnRangoConVolver(
            String mensaje, int minimo, int maximo) {

        while (true) {

            int valor = leerEntero(mensaje);

            if (valor == 0 || (valor >= minimo && valor <= maximo)) {
                return valor;
            }

            System.out.println("Ingresá 0 para volver o una opción entre "
                    + minimo + " y " + maximo + ".");
        }
    }


    private int leerEnteroNoNegativo(String mensaje) {

        while (true) {

            int valor = leerEntero(mensaje + " (VOLVER para cancelar): ");

            if (valor >= 0) {
                return valor;
            }

            System.out.println("El número no puede ser negativo.");
        }
    }


    private double leerDoubleNoNegativo(String mensaje) {

        while (true) {

            System.out.print(mensaje + " (VOLVER para cancelar): ");
            String entrada = teclado.nextLine().trim();

            if (entrada.equalsIgnoreCase("VOLVER")) {
                throw new OperacionCanceladaException();
            }

            entrada = entrada.replace(',', '.');

            try {

                double valor = Double.parseDouble(entrada);

                if (Double.isFinite(valor) && valor >= 0) {
                    return valor;
                }

                System.out.println("El valor debe ser un número válido y no negativo.");

            } catch (NumberFormatException e) {
                System.out.println("Ingresá un número válido.");
            }
        }
    }


    private String leerTexto(String mensaje) {

        while (true) {

            System.out.print(mensaje + " (0 para volver): ");
            String texto = teclado.nextLine().trim();

            if (texto.equals("0")) {
                throw new OperacionCanceladaException();
            }

            if (!texto.isBlank()) {
                return texto;
            }

            System.out.println("El dato no puede estar vacío.");
        }
    }


    private LocalDate leerFecha(String mensaje) {

        while (true) {

            System.out.print(mensaje + " (VOLVER para cancelar): ");
            String entrada = teclado.nextLine().trim();

            if (entrada.equalsIgnoreCase("VOLVER")) {
                throw new OperacionCanceladaException();
            }

            try {
                return LocalDate.parse(entrada);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Utilizá AAAA-MM-DD.");
            }
        }
    }

    private static class OperacionCanceladaException extends RuntimeException {
    }
}