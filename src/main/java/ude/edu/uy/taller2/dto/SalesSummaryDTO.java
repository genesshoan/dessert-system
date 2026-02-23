package ude.edu.uy.taller2.dto;

import java.math.BigDecimal;

/**
 * DTO que contiene el resumen de ventas para un postre: unidades y monto total.
 */
public class SalesSummaryDTO {
    private int totalUnits;
    private BigDecimal totalAmount;

    /**
     * Crea el DTO con unidades totales y monto total.
     *
     * @param totalUnits  Número total de unidades.
     * @param totalAmount Monto total acumulado.
     */
    public SalesSummaryDTO(int totalUnits, BigDecimal totalAmount) {
        this.totalUnits = totalUnits;
        this.totalAmount = totalAmount;
    }

    /** Devuelve el número total de unidades. */
    public int getTotalUnits() {
        return totalUnits;
    }

    /** Devuelve el monto total acumulado. */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
