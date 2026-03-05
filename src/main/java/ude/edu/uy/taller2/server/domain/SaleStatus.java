package ude.edu.uy.taller2.server.domain;

/**
 * Estados posibles de una venta.
 */
public enum SaleStatus {
    IN_PROGRESS,
    COMPLETED;

    @Override
    public String toString() {
        return switch (this) {
            case IN_PROGRESS -> "In progress";
            case COMPLETED -> "Completed";
        };
    }
}
