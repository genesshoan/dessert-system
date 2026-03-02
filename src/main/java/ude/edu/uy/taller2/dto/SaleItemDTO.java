package ude.edu.uy.taller2.dto;

import java.io.Serializable;

/**
 * DTO that represents an item inside a sale: quantity and the dessert descriptor.
 */
public class SaleItemDTO implements Serializable {
    private int quantity;
    private DessertDTO dessert;

    /**
     * Create a sale item DTO.
     *
     * @param quantity number of units
     * @param dessert  dessert information
     */
    public SaleItemDTO(int quantity, DessertDTO dessert) {
        this.quantity = quantity;
        this.dessert = dessert;
    }

    /** Returns the quantity for this item. */
    public int getQuantity() {
        return quantity;
    }

    /** Returns the dessert DTO for this item. */
    public DessertDTO getDessert() {
        return dessert;
    }

    @Override
    public String toString() {
        return String.format("SaleItemDTO{quantity=%d, dessert=%s}", quantity, dessert == null ? "null" : dessert.toString());
    }
}
