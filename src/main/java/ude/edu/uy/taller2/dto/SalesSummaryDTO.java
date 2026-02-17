package ude.edu.uy.taller2.dto;

import java.math.BigDecimal;

public class SalesSummaryDTO {
    private int totalUnits;
    private BigDecimal totalAmount;

    public SalesSummaryDTO(int totalUnits, BigDecimal totalAmount) {
        this.totalUnits = totalUnits;
        this.totalAmount = totalAmount;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
