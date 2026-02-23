package ude.edu.uy.taller2.persistence;

import ude.edu.uy.taller2.collection.Desserts;
import ude.edu.uy.taller2.collection.Sales;

import java.io.Serializable;

public class AppData implements Serializable {
    private Desserts desserts;
    private Sales sales;

    public AppData(Desserts desserts, Sales sales) {
        this.desserts = desserts;
        this.sales = sales;
    }

    public Desserts getDesserts() {
        return desserts;
    }

    public Sales getSales() {
        return sales;
    }
}
