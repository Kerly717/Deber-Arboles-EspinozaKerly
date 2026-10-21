package negocio;

import enums.TipoRecorrido;
import modelo.Libro;
import java.util.ArrayList;
import java.util.List;

public class CatalogoABB {
    private NodoLibro raiz;

    
    public boolean insertar(Libro libro) {
        if (libro == null || buscar(libro.getCodigo()) != null) {
            return false;
        }
        raiz = insertarRec(raiz, libro);
        return true;
    }

    private NodoLibro insertarRec(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);
        }
        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), libro));
        } else {
            nodo.setDerecho(insertarRec(nodo.getDerecho(), libro));
        }
        return nodo;
    }

    
    public Libro buscar(int codigo) {
        return buscarRec(raiz, codigo);
    }

    private Libro buscarRec(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo == actual) {
            return nodo.getDato();
        }
        return codigo < actual ? buscarRec(nodo.getIzquierdo(), codigo)
                               : buscarRec(nodo.getDerecho(), codigo);
    }

    
    public int contar() {
        return contarRec(raiz);
    }

    private int contarRec(NodoLibro nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarRec(nodo.getIzquierdo()) + contarRec(nodo.getDerecho());
    }

    
    public List<Libro> recorrer(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido no puede ser nulo.");
        }
        List<Libro> resultado = new ArrayList<>();
        recorrerRec(raiz, tipo, resultado);
        return resultado;
    }

    private void recorrerRec(NodoLibro nodo, TipoRecorrido tipo, List<Libro> resultado) {
        if (nodo == null) {
            return;
        }
        if (tipo == TipoRecorrido.PREORDEN) {
            resultado.add(nodo.getDato());
        }
        recorrerRec(nodo.getIzquierdo(), tipo, resultado);
        if (tipo == TipoRecorrido.INORDEN) {
            resultado.add(nodo.getDato());
        }
        recorrerRec(nodo.getDerecho(), tipo, resultado);
        if (tipo == TipoRecorrido.POSORDEN) {
            resultado.add(nodo.getDato());
        }
    }

    
    public String trazaRecorrido(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido no puede ser nulo.");
        }
        StringBuilder traza = new StringBuilder();
        StringBuilder secuencia = new StringBuilder();
        trazaRec(raiz, tipo, 0, traza, secuencia);
        traza.append("Secuencia ").append(tipo).append(":").append(secuencia).append("\n");
        return traza.toString();
    }

    private void trazaRec(NodoLibro nodo, TipoRecorrido tipo, int nivel,
                          StringBuilder traza, StringBuilder secuencia) {
        String sangria = "  ".repeat(nivel);
        if (nodo == null) {
            traza.append(sangria).append("null -> regresa\n");
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        traza.append(sangria).append("entra ").append(codigo).append("\n");
        if (tipo == TipoRecorrido.PREORDEN) {
            visitarTraza(codigo, sangria, traza, secuencia);
        }
        trazaRec(nodo.getIzquierdo(), tipo, nivel + 1, traza, secuencia);
        if (tipo == TipoRecorrido.INORDEN) {
            visitarTraza(codigo, sangria, traza, secuencia);
        }
        trazaRec(nodo.getDerecho(), tipo, nivel + 1, traza, secuencia);
        if (tipo == TipoRecorrido.POSORDEN) {
            visitarTraza(codigo, sangria, traza, secuencia);
        }
    }

    private void visitarTraza(int codigo, String sangria, StringBuilder traza, StringBuilder secuencia) {
        traza.append(sangria).append("VISITA ").append(codigo).append("\n");
        secuencia.append(" ").append(codigo);
    }

    
    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        Libro libro = buscar(codigo);
        if (libro == null) {
            return false;
        }
        Libro validado = new Libro(codigo, titulo, autor, anio); // valida los datos nuevos
        libro.setTitulo(validado.getTitulo());
        libro.setAutor(validado.getAutor());
        libro.setAnio(validado.getAnio());
        return true;
    }

    
    public boolean eliminar(int codigo) {
        if (buscar(codigo) == null) {
            return false;
        }
        raiz = eliminarRec(raiz, codigo);
        return true;
    }

    private NodoLibro eliminarRec(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo < actual) {
            nodo.setIzquierdo(eliminarRec(nodo.getIzquierdo(), codigo));
        } else if (codigo > actual) {
            nodo.setDerecho(eliminarRec(nodo.getDerecho(), codigo));
        } else {
            if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
                return null;                       // caso 1: hoja
            }
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();          // caso 2: solo hijo derecho
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();        // caso 2: solo hijo izquierdo
            }
            NodoLibro sucesor = minimo(nodo.getDerecho()); // caso 3: dos hijos
            nodo.setDato(sucesor.getDato());
            nodo.setDerecho(eliminarRec(nodo.getDerecho(), sucesor.getDato().getCodigo()));
        }
        return nodo;
    }

    private NodoLibro minimo(NodoLibro nodo) {
        return nodo.getIzquierdo() == null ? nodo : minimo(nodo.getIzquierdo());
    }

    public NodoLibro getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoLibro raiz) {
        this.raiz = raiz;
    }

    
    public int contarEnRango(int a, int b) {
        int minimo = Math.min(a, b);
        int maximo = Math.max(a, b);
        return contarEnRangoRec(raiz, minimo, maximo);
    }

    private int contarEnRangoRec(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo < a) {
            return contarEnRangoRec(nodo.getDerecho(), a, b);   // poda: todo el subárbol izquierdo es menor
        }
        if (codigo > b) {
            return contarEnRangoRec(nodo.getIzquierdo(), a, b); // poda: todo el subárbol derecho es mayor
        }
        return 1 + contarEnRangoRec(nodo.getIzquierdo(), a, b)
                 + contarEnRangoRec(nodo.getDerecho(), a, b);
    }

    
    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        caminoRec(raiz, codigo, camino);
        return camino;
    }

    private void caminoRec(NodoLibro nodo, int codigo, List<Integer> camino) {
        if (nodo == null) {
            return;
        }
        int actual = nodo.getDato().getCodigo();
        camino.add(actual);
        if (codigo < actual) {
            caminoRec(nodo.getIzquierdo(), codigo, camino);
        } else if (codigo > actual) {
            caminoRec(nodo.getDerecho(), codigo, camino);
        }
    }

    
    public boolean esABBValido() {
        return validoRec(raiz, null, null);
    }

    private boolean validoRec(NodoLibro nodo, Integer minimo, Integer maximo) {
        if (nodo == null) {
            return true;
        }
        if (nodo.getDato() == null) {
            return false;
        }
        int codigo = nodo.getDato().getCodigo();
        if (minimo != null && codigo <= minimo) {
            return false;
        }
        if (maximo != null && codigo >= maximo) {
            return false;
        }
        return validoRec(nodo.getIzquierdo(), minimo, codigo)
            && validoRec(nodo.getDerecho(), codigo, maximo);
    }

    
    public static CatalogoABB desdePreorden(int[] preorden) {
        if (preorden == null) {
            throw new IllegalArgumentException("El preorden no puede ser nulo.");
        }
        CatalogoABB catalogo = new CatalogoABB();
        int[] indice = {0};
        catalogo.raiz = construirRec(preorden, indice, 0, Integer.MAX_VALUE);
        if (indice[0] != preorden.length) {
            throw new IllegalArgumentException("El arreglo no es un preorden válido de un ABB.");
        }
        return catalogo;
    }

    private static NodoLibro construirRec(int[] preorden, int[] indice, int minimo, int maximo) {
        if (indice[0] >= preorden.length) {
            return null;
        }
        int codigo = preorden[indice[0]];
        if (codigo <= minimo || codigo >= maximo) {
            return null; // no pertenece a este subárbol
        }
        indice[0]++;
        NodoLibro nodo = new NodoLibro(new Libro(codigo, "Libro " + codigo, "Autor desconocido", 2000));
        nodo.setIzquierdo(construirRec(preorden, indice, minimo, codigo));
        nodo.setDerecho(construirRec(preorden, indice, codigo, maximo));
        return nodo;
    }
}
