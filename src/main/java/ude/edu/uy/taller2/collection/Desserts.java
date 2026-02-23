package ude.edu.uy.taller2.collection;

import ude.edu.uy.taller2.domain.Dessert;

import java.util.List;
import java.util.TreeMap;

/**
 * Catálogo en memoria de postres, indexados por código.
 */
public class Desserts {
    private final TreeMap<String, Dessert> desserts;

    /**
     * Crea un catálogo vacío de postres.
     */
    public Desserts() {
        desserts = new TreeMap<>();
    }

    /**
     * Indica si un código de postre forma parte del catálogo.
     *
     * @param code Código del postre.
     * @return true si el código está presente; false en caso contrario.
     */
    public boolean member(String code) {
        return desserts.containsKey(code);
    }

    /** Inserta un postre en el catálogo. */
    public void insert(Dessert dessert) {
        desserts.put(dessert.getCode(), dessert);
    }

    /** Devuelve el postre asociado al código o null si no existe. */
    public Dessert find(String code) {
        return desserts.get(code);
    }

    /** Devuelve una lista inmodificable con los postres del catálogo. */
    public List<Dessert> getDesserts() {
        return List.copyOf(desserts.values());
    }
}
