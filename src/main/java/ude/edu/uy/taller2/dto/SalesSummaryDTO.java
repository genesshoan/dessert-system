package ude.edu.uy.taller2.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO que contiene el resumen de ventas para un postre: unidades y monto total.
 */
public class SalesSummaryDTO implements Serializable {
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

    /**
     * Representación en cadena legible del resumen de ventas.
     * Formato: SalesSummaryDTO{totalUnits=10, totalAmount=1234.56}
     */
    @Override
    public String toString() {
        return String.format("SalesSummaryDTO{totalUnits=%d, totalAmount=%s}",
                totalUnits, totalAmount == null ? "0" : totalAmount.toPlainString());
    }
}
