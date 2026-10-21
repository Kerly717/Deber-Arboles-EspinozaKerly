package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static ArbolBiblioteca arbol = null;

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elija una opción: ", -1);
            try {
                ejecutarOpcion(opcion);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
        System.out.println("Hasta pronto.");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== Catálogo Biblioteca FISEI =====");
        System.out.println(" 1. Cargar la estructura inicial");
        System.out.println(" 2. Mostrar la jerarquía");
        System.out.println(" 3. Agregar una unidad");
        System.out.println(" 4. Buscar una unidad");
        System.out.println(" 5. Consultar altura, hojas y total de libros");
        System.out.println(" 6. Consultar el nivel de una unidad");
        System.out.println(" 7. Insertar un libro en un estante");
        System.out.println(" 8. Buscar un libro de un estante");
        System.out.println(" 9. Modificar un libro de un estante");
        System.out.println("10. Eliminar un libro de un estante");
        System.out.println("11. Mostrar recorridos con su traza");
        System.out.println("12. Ejecutar los retos");
        System.out.println(" 0. Salir");
    }

    private static void ejecutarOpcion(int opcion) {
        if (opcion == 0) {
            return;
        }
        if (opcion == 1) {
            arbol = DatosPrueba.crearBibliotecaInicial();
            System.out.println("Estructura inicial cargada: 7 unidades y 13 libros.");
            return;
        }
        if (opcion < 1 || opcion > 12) {
            System.out.println("Opción no válida.");
            return;
        }
        if (arbol == null) {
            System.out.println("Primero cargue la estructura inicial (opción 1).");
            return;
        }
        switch (opcion) {
            case 2:
                System.out.print(arbol.mostrarJerarquia());
                break;
            case 3:
                agregarUnidad();
                break;
            case 4:
                buscarUnidad();
                break;
            case 5:
                System.out.println("Altura (aristas): " + arbol.altura());
                System.out.println("Hojas: " + arbol.contarHojas());
                System.out.println("Total de libros: " + arbol.contarLibrosTotal());
                break;
            case 6:
                String codigo = leerTexto("Código de la unidad: ");
                System.out.println("Nivel de " + codigo.toUpperCase() + ": " + arbol.nivel(codigo));
                break;
            case 7:
                insertarLibro();
                break;
            case 8:
                buscarLibro();
                break;
            case 9:
                modificarLibro();
                break;
            case 10:
                eliminarLibro();
                break;
            case 11:
                mostrarRecorridos();
                break;
            case 12:
                ejecutarRetos();
                break;
            default:
                break;
        }
    }

    private static void agregarUnidad() {
        String padre = leerTexto("Código de la unidad padre: ");
        String codigo = leerTexto("Código de la nueva unidad: ");
        String nombre = leerTexto("Nombre de la nueva unidad: ");
        String tipoTexto = leerTexto("Tipo (BIBLIOTECA, AREA o ESTANTE): ").toUpperCase();
        TipoUnidad tipo;
        try {
            tipo = TipoUnidad.valueOf(tipoTexto);
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo no válido.");
            return;
        }
        UnidadBiblioteca nueva = new UnidadBiblioteca(codigo, nombre, tipo);
        if (arbol.agregar(padre, nueva)) {
            System.out.println("Unidad agregada: " + nueva);
        } else {
            System.out.println("No se pudo agregar: el padre no existe, el código está repetido o la jerarquía no lo permite.");
        }
    }

    private static void buscarUnidad() {
        String codigo = leerTexto("Código de la unidad: ");
        NodoUnidad nodo = arbol.buscar(codigo);
        if (nodo == null) {
            System.out.println("No existe una unidad con el código " + codigo.toUpperCase() + ".");
            return;
        }
        System.out.println("Encontrada: " + nodo.getDato() + " (" + nodo.getDato().getTipo() + "), hijos: "
                + nodo.getHijos().size());
        if (nodo.getCatalogo() != null) {
            System.out.println("Libros en el estante: " + nodo.getCatalogo().contar());
        }
    }

    
    private static CatalogoABB pedirCatalogo() {
        String codigo = leerTexto("Código del estante (por ejemplo SW-E1): ");
        NodoUnidad nodo = arbol.buscar(codigo);
        if (nodo == null) {
            System.out.println("No existe la unidad " + codigo.toUpperCase() + ".");
            return null;
        }
        if (nodo.getCatalogo() == null) {
            System.out.println(nodo.getDato().getCodigo() + " no es un ESTANTE, no tiene libros.");
            return null;
        }
        return nodo.getCatalogo();
    }

    private static void insertarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        int codigo = leerEntero("Código del libro (entero > 0): ", 0);
        String titulo = leerTexto("Título: ");
        String autor = leerTexto("Autor: ");
        int anio = leerEntero("Año (1900-2026): ", 0);
        Libro libro = new Libro(codigo, titulo, autor, anio);
        System.out.println(catalogo.insertar(libro) ? "Libro insertado." : "El código ya existe: no se insertó.");
    }

    private static void buscarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        Libro libro = catalogo.buscar(leerEntero("Código del libro: ", 0));
        System.out.println(libro == null ? "No existe ese libro en el estante." : "Encontrado: " + libro);
    }

    private static void modificarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        int codigo = leerEntero("Código del libro a modificar: ", 0);
        String titulo = leerTexto("Nuevo título: ");
        String autor = leerTexto("Nuevo autor: ");
        int anio = leerEntero("Nuevo año (1900-2026): ", 0);
        System.out.println(catalogo.modificar(codigo, titulo, autor, anio)
                ? "Libro modificado." : "No existe ese libro en el estante.");
    }

    private static void eliminarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        int codigo = leerEntero("Código del libro a eliminar: ", 0);
        System.out.println(catalogo.eliminar(codigo) ? "Libro eliminado." : "No existe ese libro en el estante.");
    }

    private static void mostrarRecorridos() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        for (TipoRecorrido tipo : TipoRecorrido.values()) {
            System.out.println();
            System.out.println("--- " + tipo + " ---");
            System.out.print(catalogo.trazaRecorrido(tipo));
        }
    }

    private static void ejecutarRetos() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        System.out.println("1) contarEnRango(a, b)");
        int a = leerEntero("   a: ", 0);
        int b = leerEntero("   b: ", 0);
        System.out.println("   Códigos entre " + a + " y " + b + ": " + catalogo.contarEnRango(a, b));
        System.out.println("2) caminoBusqueda(codigo)");
        int codigo = leerEntero("   Código: ", 0);
        System.out.println("   Camino: " + catalogo.caminoBusqueda(codigo));
        System.out.println("3) esABBValido(): " + catalogo.esABBValido());
        System.out.println("4) Extra desdePreorden(int[])");
        String linea = leerTexto("   Preorden separado por espacios (Enter para omitir): ");
        if (linea.isEmpty()) {
            return;
        }
        String[] partes = linea.split("\\s+");
        int[] preorden = new int[partes.length];
        try {
            for (int i = 0; i < partes.length; i++) {
                preorden[i] = Integer.parseInt(partes[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("   Solo se admiten números enteros.");
            return;
        }
        CatalogoABB reconstruido = CatalogoABB.desdePreorden(preorden);
        System.out.println("   Reconstruido. Preorden: " + codigosComoTexto(reconstruido.recorrer(TipoRecorrido.PREORDEN))
                + " | Inorden: " + codigosComoTexto(reconstruido.recorrer(TipoRecorrido.INORDEN)));
    }

    private static String codigosComoTexto(List<Libro> libros) {
        StringBuilder sb = new StringBuilder();
        for (Libro libro : libros) {
            sb.append(libro.getCodigo()).append(" ");
        }
        return sb.toString().trim();
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return SC.hasNextLine() ? SC.nextLine().trim() : "";
    }

    
    private static int leerEntero(String mensaje, int porDefecto) {
        String texto = leerTexto(mensaje);
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            System.out.println("Entrada no numérica.");
            return porDefecto;
        }
    }
}
