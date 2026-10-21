package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PruebasBiblioteca {
    private static int total = 0;
    private static int correctas = 0;

    public static void main(String[] args) {
        ejecutar();
    }

    
    public static boolean ejecutar() {
        total = 0;
        correctas = 0;
        System.out.println("=== PruebasBiblioteca ===");

        ArbolBiblioteca soloRaiz = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        verificar("1a. Solo raíz: altura 0", soloRaiz.altura() == 0);
        verificar("1b. Solo raíz: 1 hoja", soloRaiz.contarHojas() == 1);

        ArbolBiblioteca arbol = DatosPrueba.crearBibliotecaInicial();
        verificar("2a. Inicial: altura 2", arbol.altura() == 2);
        verificar("2b. Inicial: 4 hojas", arbol.contarHojas() == 4);
        verificar("2c. Inicial: 13 libros", arbol.contarLibrosTotal() == 13);

        verificar("3a. nivel(BIB) = 0", arbol.nivel("BIB") == 0);
        verificar("3b. nivel(sw) = 1 (sin distinguir mayúsculas)", arbol.nivel("sw") == 1);
        verificar("3c. nivel(SW-E2) = 2", arbol.nivel("SW-E2") == 2);
        verificar("3d. nivel(XYZ) = -1", arbol.nivel("XYZ") == -1);

        boolean agregado = arbol.agregar("BIB", new UnidadBiblioteca("E-X", "Estante suelto", TipoUnidad.ESTANTE));
        verificar("4a. Agregar ESTANTE bajo BIBLIOTECA = false", !agregado);
        verificar("4b. Agregar código repetido = false",
                !arbol.agregar("BIB", new UnidadBiblioteca("sw", "Otra área", TipoUnidad.AREA)));
        verificar("4c. Agregar bajo un padre inexistente = false",
                !arbol.agregar("NOEXISTE", new UnidadBiblioteca("A1", "Área", TipoUnidad.AREA)));

        CatalogoABB swE1 = arbol.buscar("SW-E1").getCatalogo();
        verificar("5a. Preorden SW-E1", codigos(swE1.recorrer(TipoRecorrido.PREORDEN))
                .equals(Arrays.asList(60, 35, 20, 45, 40, 80, 70, 75, 90)));
        verificar("5b. Inorden SW-E1", codigos(swE1.recorrer(TipoRecorrido.INORDEN))
                .equals(Arrays.asList(20, 35, 40, 45, 60, 70, 75, 80, 90)));
        verificar("5c. Posorden SW-E1", codigos(swE1.recorrer(TipoRecorrido.POSORDEN))
                .equals(Arrays.asList(20, 40, 45, 35, 75, 70, 90, 80, 60)));
        verificar("5d. Última línea de la traza = Secuencia PREORDEN: ...",
                ultimaLinea(swE1.trazaRecorrido(TipoRecorrido.PREORDEN))
                        .equals("Secuencia PREORDEN: 60 35 20 45 40 80 70 75 90"));

        verificar("6. Insertar código repetido = false",
                !swE1.insertar(new Libro(60, "Repetido", "Alguien", 2000)));

        verificar("7a. caminoBusqueda(42) = [60, 35, 45, 40]",
                swE1.caminoBusqueda(42).equals(Arrays.asList(60, 35, 45, 40)));
        verificar("7b. contarEnRango(40, 75) = 5", swE1.contarEnRango(40, 75) == 5);

        boolean inserto42 = swE1.insertar(new Libro(42, "Libro de prueba", "Autor de prueba", 2020));
        boolean elimino20 = swE1.eliminar(20);
        boolean elimino70 = swE1.eliminar(70);
        boolean elimino60 = swE1.eliminar(60);
        verificar("8a. Insertar 42 y eliminar 20, 70 y 60 devuelven true",
                inserto42 && elimino20 && elimino70 && elimino60);
        verificar("8b. Preorden final = 75 35 45 40 42 80 90", codigos(swE1.recorrer(TipoRecorrido.PREORDEN))
                .equals(Arrays.asList(75, 35, 45, 40, 42, 80, 90)));
        verificar("8c. El ABB sigue siendo válido tras eliminar", swE1.esABBValido());
        verificar("8d. Quedan 7 libros", swE1.contar() == 7);

        verificar("9a. buscar(999) = null", swE1.buscar(999) == null);
        verificar("9b. modificar(999) = false", !swE1.modificar(999, "T", "A", 2000));
        verificar("9c. eliminar(999) = false", !swE1.eliminar(999));
        verificar("9d. modificar un libro existente = true y cambia los datos",
                swE1.modificar(75, "Título nuevo", "Autor nuevo", 2010)
                        && swE1.buscar(75).getTitulo().equals("Título nuevo")
                        && swE1.buscar(75).getAnio() == 2010
                        && swE1.buscar(75).getCodigo() == 75);

        verificar("10a. Libro con código 0 -> IllegalArgumentException",
                lanzaIAE(() -> new Libro(0, "T", "A", 2000)));
        verificar("10b. Libro con título vacío -> IllegalArgumentException",
                lanzaIAE(() -> new Libro(1, " ", "A", 2000)));
        verificar("10c. Libro con autor nulo -> IllegalArgumentException",
                lanzaIAE(() -> new Libro(1, "T", null, 2000)));
        verificar("10d. Libro con año 1899 -> IllegalArgumentException",
                lanzaIAE(() -> new Libro(1, "T", "A", 1899)));
        verificar("10e. Libro con año 2027 -> IllegalArgumentException",
                lanzaIAE(() -> new Libro(1, "T", "A", 2027)));

        CatalogoABB malo = new CatalogoABB();
        NodoLibro n50 = new NodoLibro(new Libro(50, "Raíz", "A", 2000));
        NodoLibro n30 = new NodoLibro(new Libro(30, "Hijo", "A", 2000));
        NodoLibro n60 = new NodoLibro(new Libro(60, "Nieto mal ubicado", "A", 2000));
        n50.setIzquierdo(n30);
        n30.setDerecho(n60); // 60 > 30 (parece correcto) pero 60 > 50 y está en el subárbol izquierdo
        malo.setRaiz(n50);
        verificar("11a. esABBValido con nieto mal ubicado = false", !malo.esABBValido());
        verificar("11b. esABBValido en un ABB bien formado = true", DatosPrueba.crearBibliotecaInicial()
                .buscar("SW-E1").getCatalogo().esABBValido());

        CatalogoABB reconstruido = CatalogoABB.desdePreorden(new int[]{60, 35, 20, 45, 40, 80, 70, 75, 90});
        verificar("12a. desdePreorden reproduce el mismo preorden", codigos(reconstruido.recorrer(TipoRecorrido.PREORDEN))
                .equals(Arrays.asList(60, 35, 20, 45, 40, 80, 70, 75, 90)));
        verificar("12b. desdePreorden reproduce el mismo inorden", codigos(reconstruido.recorrer(TipoRecorrido.INORDEN))
                .equals(Arrays.asList(20, 35, 40, 45, 60, 70, 75, 80, 90)));
        verificar("12c. desdePreorden con un arreglo inválido -> IllegalArgumentException",
                lanzaIAE(() -> CatalogoABB.desdePreorden(new int[]{5, 3, 3})));

        verificar("13a. Eliminar una hoja", eliminarYComparar(new int[]{50, 30, 70}, 30, Arrays.asList(50, 70)));
        verificar("13b. Eliminar un nodo con un hijo", eliminarYComparar(new int[]{50, 30, 20}, 30, Arrays.asList(50, 20)));
        verificar("13c. Eliminar un nodo con dos hijos (sucesor inorden)",
                eliminarYComparar(new int[]{50, 30, 70, 60, 80}, 50, Arrays.asList(60, 30, 70, 80)));
        verificar("14. mostrarJerarquia lista las 7 unidades en preorden", DatosPrueba.crearBibliotecaInicial()
                .mostrarJerarquia().equals("BIB Biblioteca FISEI\n"
                        + "  SW Software\n"
                        + "    SW-E1 Estante 1 de Software\n"
                        + "    SW-E2 Estante 2 de Software\n"
                        + "  TI Tecnologías de la Información\n"
                        + "    TI-E1 Estante 1 de TI\n"
                        + "  TEL Telecomunicaciones\n"));

        System.out.println();
        System.out.println(correctas + "/" + total + " pruebas OK");
        return correctas == total;
    }

    private static void verificar(String nombre, boolean ok) {
        total++;
        if (ok) {
            correctas++;
        }
        System.out.println((ok ? "[OK]    " : "[FALLA] ") + nombre);
    }

    private static List<Integer> codigos(List<Libro> libros) {
        List<Integer> lista = new ArrayList<>();
        for (Libro libro : libros) {
            lista.add(libro.getCodigo());
        }
        return lista;
    }

    private static String ultimaLinea(String texto) {
        String[] lineas = texto.split("\n");
        return lineas[lineas.length - 1];
    }

    private static boolean lanzaIAE(Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    
    private static boolean eliminarYComparar(int[] insertar, int eliminar, List<Integer> preordenEsperado) {
        CatalogoABB abb = new CatalogoABB();
        for (int codigo : insertar) {
            abb.insertar(new Libro(codigo, "T" + codigo, "A", 2000));
        }
        return abb.eliminar(eliminar) && codigos(abb.recorrer(TipoRecorrido.PREORDEN)).equals(preordenEsperado);
    }
}
