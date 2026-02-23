package ude.edu.uy.taller2.domain;

import ude.edu.uy.taller2.dto.SalesSummaryDTO;

import java.math.BigDecimal;

/**
 * Representa un item dentro de una venta: un postre y la cantidad vendida.
 * Proporciona utilidades para obtener el monto total del item y su resumen.
 */
public class SaleItem {
    private int quantity;
    private Dessert dessert;

    /**
     * Crea un nuevo item de venta para un postre dado con la cantidad especificada.
     *
     * @param quantity Cantidad de unidades del postre.
     * @param dessert  Postre asociado al item.
     */
    public SaleItem(int quantity, Dessert dessert) {
        this.quantity = quantity;
        this.dessert = dessert;
    }

    /** Devuelve la cantidad de unidades de este item. */
    public int getQuantity() {
        return quantity;
    }

    /** Devuelve el postre asociado a este item. */
    public Dessert getDessert(){
        return dessert;
    }

    /**
     * Calcula y devuelve el monto total de este item (precio del postre * cantidad).
     *
     * @return Monto total del item como BigDecimal.
     */
    public BigDecimal getTotalAmount() {
        return dessert.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    /** Establece la cantidad de unidades de este item. */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Devuelve un DTO con el resumen de este item: unidades y monto.
     *
     * @return SalesSummaryDTO con totalUnits y totalAmount del item.
     */
    public SalesSummaryDTO saleSummary(){
        return new SalesSummaryDTO(quantity, getTotalAmount());
    }

    @Override
    public String toString() {
        return "SaleItem{" +
                "quantity=" + quantity +
                ", dessert=" + dessert.getCode() +
                '}';
    }
}
