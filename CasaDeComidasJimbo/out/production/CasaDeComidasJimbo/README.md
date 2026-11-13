# CASA DE COMIDAS JIMBO
## Proyecto final – Programación Orientada a Objetos en Java

### 1. Descripción del proyecto

Casa de comidas Jimbo es un sistema de gestión para un negocio gastronómico que permite administrar productos, clientes y pedidos mediante una aplicación de consola desarrollada en Java.

El sistema permite registrar platos y bebidas, administrar sus precios y stock, registrar clientes, generar pedidos para delivery o retiro en el local, modificar pedidos, gestionar sus estados y pagos, realizar búsquedas y consultar reportes de ventas.

El objetivo es organizar las operaciones principales del negocio aplicando los conceptos de programación orientada a objetos, encapsulamiento, asociación, agregación, composición, herencia, abstracción, polimorfismo e interfaces.

### 2. Organización del sistema

El proyecto está dividido en tres partes:

- **Modelo:** contiene las clases que representan los elementos del negocio y sus comportamientos.
- **Gestión:** contiene la clase `CasaDeComidasJimbo`, responsable de administrar los productos, clientes y pedidos.
- **Consola:** contiene la clase `Consola`, encargada de mostrar el menú, recibir los datos y comunicar los resultados al usuario.

La clase `Main` inicializa los objetos de prueba y pone en funcionamiento el menú.

El sistema cuenta con ocho clases de dominio:

`Producto`, `Plato`, `Bebida`, `Cliente`, `DetallePedido`, `Pedido`, `PedidoDelivery` y `PedidoRetiro`.

### 3. Justificación de las jerarquías de herencia

**Primera jerarquía: Producto**

- `Producto` es una clase abstracta.
- `Plato` hereda de `Producto`.
- `Bebida` hereda de `Producto`.

La herencia está justificada porque un plato es un producto y una bebida también es un producto. Ambos comparten características generales, como código, nombre, precio y stock, pero cada uno posee características particulares.

`Plato` incorpora el tipo de plato y su fecha de disponibilidad. `Bebida` incorpora su presentación.

**Segunda jerarquía: Pedido**

- `Pedido` es una clase abstracta.
- `PedidoDelivery` hereda de `Pedido`.
- `PedidoRetiro` hereda de `Pedido`.

La herencia está justificada porque un pedido de delivery es un pedido y un pedido para retirar en el local también es un pedido.

Ambos comparten información y operaciones generales, pero se diferencian en la forma de entrega y en el cálculo del importe final. `PedidoDelivery` contempla el costo de envío, mientras que `PedidoRetiro` utiliza la modalidad de retiro.

### 4. Abstracción y polimorfismo

El sistema utiliza clases abstractas para representar conceptos generales que no deben instanciarse directamente.

La clase `Pedido` declara el método abstracto `calcularTotal()`, que es implementado de manera diferente por sus dos subclases.

- `PedidoDelivery` calcula el total sumando los subtotales de los productos y el costo de envío.
- `PedidoRetiro` calcula el total considerando únicamente los productos del pedido.

El polimorfismo se utiliza en la colección de pedidos administrada por `CasaDeComidasJimbo`. Al recorrer una colección de tipo `Pedido` y ejecutar `calcularTotal()`, Java utiliza automáticamente la implementación correspondiente al tipo real de cada objeto.

Esto permite trabajar con diferentes clases de pedidos sin tener que preguntar previamente de qué tipo son.

### 5. Interfaz Buscable

El proyecto implementa la interfaz propia `Buscable`, que establece un contrato para los objetos que pueden ser localizados mediante un criterio de búsqueda.

Las clases `Producto` y `Cliente` implementan esta interfaz.

Ambas clases pertenecen a ramas diferentes del modelo y comparten la capacidad de ser buscadas, aunque representan elementos distintos del negocio.

La clase `CasaDeComidasJimbo` utiliza el tipo `Buscable` para realizar búsquedas de manera uniforme.

### 6. Relaciones entre clases

El proyecto utiliza diferentes relaciones:

- **Agregación:** `CasaDeComidasJimbo` administra colecciones de productos, clientes y pedidos. Estos objetos pueden existir independientemente de la clase gestora.
- **Composición:** `Pedido` contiene una colección de `DetallePedido`. Los detalles forman parte del pedido y no tienen sentido como elementos independientes dentro del sistema.
- **Asociación:** `Pedido` conoce al `Cliente` que lo realiza. Además, cada `DetallePedido` referencia al `Producto` correspondiente.

Estas relaciones permiten distribuir las responsabilidades y favorecer la colaboración entre los objetos.

### 7. Tabla de trazabilidad de requisitos

| Requisito | Clase y método donde se cumple |
|---|---|
| RM-01 | Proyecto completo. Compilación con `javac` y ejecución con `java`. |
| RM-02 | Modelo: ocho clases de dominio. |
| RM-03 | Paquetes `modelo`, `gestion` y `consola`; clases `CasaDeComidasJimbo` y `Consola`. |
| RM-04 | Clases del paquete `modelo`: no realizan entrada ni salida por consola. |
| RM-05 | `Main.main()`: inicializa datos y ejecuta el menú. |
| RM-06 | Clases de dominio: atributos de instancia privados. |
| RM-07 | Getters y métodos de modificación de las clases de dominio. |
| RM-08 | Validaciones en operaciones de modificación de productos, clientes y pedidos. |
| RM-09 | Métodos de acción como `reponerStock()`, `descontarStock()`, `agregarProducto()` y `registrarPago()`. |
| RM-10 | Clase `Plato`: constructores sobrecargados y encadenamiento con `this()`. |
| RM-11 | `CasaDeComidasJimbo`: colecciones de productos, clientes y pedidos. `Pedido`: composición con detalles. |
| RM-12 | `Pedido`: asociación con `Cliente` y `DetallePedido`: asociación con `Producto`. |
| RM-13 | `CasaDeComidasJimbo`: operaciones que coordinan pedidos, clientes, productos y detalles. |
| RM-14 | Jerarquías abstractas `Producto` y `Pedido`, cada una con dos subclases concretas. |
| RM-15 | Herencias `Plato` y `Bebida` respecto de `Producto`; `PedidoDelivery` y `PedidoRetiro` respecto de `Pedido`. |
| RM-16 | Constructores de las subclases: llamadas a `super()`. |
| RM-17 | `Plato`, `Bebida`, `PedidoDelivery` y `PedidoRetiro`: atributos y comportamientos propios. |
| RM-18 | Clases de dominio: sobrescritura de `toString()` mediante `@Override`. |
| RM-19 | `Pedido.calcularTotal()`: implementaciones diferentes en las subclases. |
| RM-20 | `CasaDeComidasJimbo`: recorrido polimórfico de la colección de pedidos para el reporte de ventas. |
| RM-21 | `Buscable`: implementada por `Producto` y `Cliente`, utilizada en las búsquedas de la clase gestora. |
| RM-22 | `CasaDeComidasJimbo.registrarProducto()`: uso de `instanceof Plato` para controlar el límite de menús especiales. |
| RM-23 | `Consola`: menú interactivo con `Scanner` y repetición hasta seleccionar salir. |
| RM-24 | `Consola`: opciones de alta, listado, búsqueda, modificación, operaciones del negocio y reportes. |
| RM-25 | `Main.main()`: inicialización de productos, clientes y pedidos de prueba. |
| RM-26 | `Consola`: validación de entradas numéricas, opciones y códigos inexistentes. |
| RM-27 | Todas las clases: convenciones de nombres de Java. |
| RM-28 | Clases de dominio: documentación Javadoc en clases y métodos públicos. |
| RM-29 | Constante `MAX_MENUS_ESPECIALES_POR_DIA` y organización de responsabilidades para evitar duplicaciones innecesarias. |

### 8. Compilación y ejecución

El proyecto utiliza Java estándar y no requiere librerías externas.

Para compilar desde PowerShell, ubicarse en la carpeta principal del proyecto y ejecutar:

```powershell
javac -d out (Get-ChildItem -Recurse -Filter *.java .\src | ForEach-Object FullName)
```

Para ejecutar el programa:

```powershell
java -cp out Main
```

El programa mostrará el menú principal y permitirá acceder a las distintas operaciones del sistema.

### 9. Uso de herramientas de inteligencia artificial

Se utilizaron herramientas de inteligencia artificial como apoyo durante el desarrollo del proyecto para consultar conceptos de programación orientada a objetos, resolver dudas sobre Java, revisar errores, colaborar en la organización del código, colaborar con el diseño y redactado del README y el diagrama UML.
