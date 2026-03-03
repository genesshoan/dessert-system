package ude.edu.uy.taller2.persistence;

import ude.edu.uy.taller2.collection.Desserts;
import ude.edu.uy.taller2.collection.Sales;

import java.io.Serializable;

/**
 * Contenedor simple que agrupa las colecciones persistibles de la aplicación:
 * catálogo de postres y conjunto de ventas. Se utiliza para serializar el
 * estado completo de la aplicación.
 */
public class AppData implements Serializable {
    private final Desserts desserts;
    private final Sales sales;

    /**
     * Crea una instancia con las colecciones provistas.
     *
     * @param desserts catálogo de postres
     * @param sales    colección de ventas
     */
    public AppData(Desserts desserts, Sales sales) {
        this.desserts = desserts;
        this.sales = sales;
    }

    /**
     * Devuelve el catálogo de postres almacenado.
     *
     * @return {@link Desserts}
     */
    public Desserts getDesserts() {
        return desserts;
    }

    /**
     * Devuelve la colección de ventas almacenada.
     *
     * @return {@link Sales}
     */
    public Sales getSales() {
        return sales;
    }
}
