package ude.edu.uy.taller2.dto;

import ude.edu.uy.taller2.domain.Dessert;

import java.io.Serializable;

public class SaleItemDTO implements Serializable {
    private int quantity;
    private Dessert dessert;

    public SaleItemDTO(int quantity, Dessert dessert) {
        this.quantity = quantity;
        this.dessert = dessert;
    }

    public int getQuantity() {
        return quantity;
    }

    public Dessert getDessert() {
        return dessert;
    }
}
