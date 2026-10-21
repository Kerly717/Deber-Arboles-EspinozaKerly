package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;

public class DatosPrueba {

    
    public static final int[] CODIGOS_SW_E1 = {60, 35, 80, 20, 45, 70, 90, 40, 75};

    private DatosPrueba() {
    }

    public static ArbolBiblioteca crearBibliotecaInicial() {
        ArbolBiblioteca arbol = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        arbol.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        arbol.agregar("BIB", new UnidadBiblioteca("TI", "Tecnologías de la Información", TipoUnidad.AREA));
        arbol.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E1", "Estante 1 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E2", "Estante 2 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));

        CatalogoABB swE1 = arbol.buscar("SW-E1").getCatalogo();
        swE1.insertar(new Libro(60, "Clean Code", "Robert C. Martin", 2008));
        swE1.insertar(new Libro(35, "Design Patterns", "Erich Gamma et al.", 1994));
        swE1.insertar(new Libro(80, "Refactoring", "Martin Fowler", 1999));
        swE1.insertar(new Libro(20, "Code Complete", "Steve McConnell", 1993));
        swE1.insertar(new Libro(45, "The Pragmatic Programmer", "Andrew Hunt y David Thomas", 1999));
        swE1.insertar(new Libro(70, "Effective Java", "Joshua Bloch", 2001));
        swE1.insertar(new Libro(90, "Introduction to Algorithms", "Thomas H. Cormen et al.", 1990));
        swE1.insertar(new Libro(40, "Software Engineering", "Ian Sommerville", 1982));
        swE1.insertar(new Libro(75, "Head First Design Patterns", "Eric Freeman y Elisabeth Robson", 2004));

        CatalogoABB swE2 = arbol.buscar("SW-E2").getCatalogo();
        swE2.insertar(new Libro(15, "The Mythical Man-Month", "Frederick P. Brooks Jr.", 1975));
        swE2.insertar(new Libro(10, "Test-Driven Development: By Example", "Kent Beck", 2002));
        swE2.insertar(new Libro(25, "Structure and Interpretation of Computer Programs", "Harold Abelson y Gerald Sussman", 1985));

        CatalogoABB tiE1 = arbol.buscar("TI-E1").getCatalogo();
        tiE1.insertar(new Libro(50, "Computer Networks", "Andrew S. Tanenbaum", 1981));
        return arbol;
    }
}
