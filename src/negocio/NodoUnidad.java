package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;
import java.util.ArrayList;
import java.util.List;

public class NodoUnidad {
    private final UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos;
    private final CatalogoABB catalogo;

    public NodoUnidad(UnidadBiblioteca dato) {
        if (dato == null) {
            throw new IllegalArgumentException("La unidad no puede ser nula.");
        }
        this.dato = dato;
        this.hijos = new ArrayList<>();
        this.catalogo = (dato.getTipo() == TipoUnidad.ESTANTE) ? new CatalogoABB() : null;
    }

    public UnidadBiblioteca getDato() {
        return dato;
    }

    public List<NodoUnidad> getHijos() {
        return hijos;
    }

    
    public CatalogoABB getCatalogo() {
        return catalogo;
    }

    public boolean esHoja() {
        return hijos.isEmpty();
    }
}
