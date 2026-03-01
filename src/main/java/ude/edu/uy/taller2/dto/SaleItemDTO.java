package ude.edu.uy.taller2.dto;

import java.io.Serializable;

public class SaleItemDTO implements Serializable {
    private int quantity;
    private DessertDTO dessert;

    public SaleItemDTO(int quantity, DessertDTO dessert) {
        this.quantity = quantity;
        this.dessert = dessert;
    }

    public int getQuantity() {
        return quantity;
    }

    public DessertDTO getDessert() {
        return dessert;
    }

    @Override
    public String toString() {
        return String.format("SaleItemDTO{quantity=%d, dessert=%s}", quantity, dessert == null ? "null" : dessert.toString());
    }
}
