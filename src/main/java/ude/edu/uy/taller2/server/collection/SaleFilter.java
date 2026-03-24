package ude.edu.uy.taller2.server.collection;

/**
 * Filtros disponibles para listar ventas por estado.
 */
public enum SaleFilter {
    ALL,
    PENDING,
    FINISHED;

    @Override
    public String toString() {
        return switch (this) {
            case ALL -> "All";
            case PENDING -> "Pending";
            case FINISHED -> "Finished";
        };
    }
}