package ude.edu.uy.taller2.server.collection;

/**
 * Filtros disponibles para listar ventas por estado.
 */
public enum SaleFilter {
    ALL,
    PENDING,
    FINISHED;

    public static SaleFilter fromString(String value) {
        return switch (value) {
            case "Finished" -> FINISHED;
            case "Pending" -> PENDING;
            default -> ALL;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case ALL -> "All";
            case PENDING -> "Pending";
            case FINISHED -> "Finished";
        };
    }
}