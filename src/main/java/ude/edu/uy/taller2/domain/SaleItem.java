package ude.edu.uy.taller2.domain;

import ude.edu.uy.taller2.dto.SaleSummaryDTO;

import java.math.BigDecimal;

public class SaleItem {
    private int quantity;
    private Dessert dessert;

    public SaleItem(int quantity, Dessert dessert) {
        this.quantity = quantity;
        this.dessert = dessert;
    }

    public int getQuantity() {
        return quantity;
    }

    public Dessert getDessert(){
        return dessert;
    }

    public BigDecimal getTotalAmount() {
        return dessert.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public SaleSummaryDTO saleSummary(){
        return new SaleSummaryDTO(quantity, getTotalAmount());
    }
}
