package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

public class ArbolBiblioteca {
    private final NodoUnidad raiz;

    public ArbolBiblioteca(UnidadBiblioteca raiz) {
        if (raiz == null || raiz.getTipo() != TipoUnidad.BIBLIOTECA) {
            throw new IllegalArgumentException("La raíz debe ser una unidad de tipo BIBLIOTECA.");
        }
        this.raiz = new NodoUnidad(raiz);
    }

    public NodoUnidad getRaiz() {
        return raiz;
    }

    
    public boolean agregar(String codigoPadre, UnidadBiblioteca nueva) {
        if (codigoPadre == null || nueva == null) {
            return false;
        }
        NodoUnidad padre = buscar(codigoPadre);
        if (padre == null || buscar(nueva.getCodigo()) != null) {
            return false;
        }
        if (!padre.getDato().getTipo().puedeTenerHijo(nueva.getTipo())) {
            return false;
        }
        padre.getHijos().add(new NodoUnidad(nueva));
        return true;
    }

    
    public NodoUnidad buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        return buscarRec(raiz, codigo.trim().toUpperCase());
    }

    private NodoUnidad buscarRec(NodoUnidad nodo, String codigo) {
        if (nodo.getDato().getCodigo().equals(codigo)) {
            return nodo;
        }
        for (NodoUnidad hijo : nodo.getHijos()) {
            NodoUnidad encontrado = buscarRec(hijo, codigo);
            if (encontrado != null) {
                return encontrado;
            }
        }
        return null;
    }

    
    public int altura() {
        return alturaRec(raiz);
    }

    private int alturaRec(NodoUnidad nodo) {
        int maxHijos = -1;
        for (NodoUnidad hijo : nodo.getHijos()) {
            maxHijos = Math.max(maxHijos, alturaRec(hijo));
        }
        return 1 + maxHijos; // sin hijos: 1 + (-1) = 0
    }

    
    public int contarHojas() {
        return contarHojasRec(raiz);
    }

    private int contarHojasRec(NodoUnidad nodo) {
        if (nodo.esHoja()) {
            return 1;
        }
        int total = 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarHojasRec(hijo);
        }
        return total;
    }

    
    public int nivel(String codigo) {
        if (codigo == null) {
            return -1;
        }
        return nivelRec(raiz, codigo.trim().toUpperCase(), 0);
    }

    private int nivelRec(NodoUnidad nodo, String codigo, int nivelActual) {
        if (nodo.getDato().getCodigo().equals(codigo)) {
            return nivelActual;
        }
        for (NodoUnidad hijo : nodo.getHijos()) {
            int nivelHijo = nivelRec(hijo, codigo, nivelActual + 1);
            if (nivelHijo != -1) {
                return nivelHijo;
            }
        }
        return -1;
    }

    
    public String mostrarJerarquia() {
        StringBuilder sb = new StringBuilder();
        jerarquiaRec(raiz, 0, sb);
        return sb.toString();
    }

    private void jerarquiaRec(NodoUnidad nodo, int nivel, StringBuilder sb) {
        sb.append("  ".repeat(nivel)).append(nodo.getDato()).append("\n");
        for (NodoUnidad hijo : nodo.getHijos()) {
            jerarquiaRec(hijo, nivel + 1, sb);
        }
    }

    
    public int contarLibrosTotal() {
        return contarLibrosRec(raiz);
    }

    private int contarLibrosRec(NodoUnidad nodo) {
        int total = (nodo.getCatalogo() != null) ? nodo.getCatalogo().contar() : 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarLibrosRec(hijo);
        }
        return total;
    }
}
