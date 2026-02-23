package ude.edu.uy.taller2.domain;

import java.math.BigDecimal;

/**
 * Subclase de Dessert que representa un postre light con edulcorante y descripción.
 */
public class LightDessert extends Dessert{
    private String sweetener;
    private String description;

    /**
     * Crea un postre light con los atributos adicionales.
     *
     * @param code        Código del postre.
     * @param name        Nombre del postre.
     * @param price       Precio del postre.
     * @param sweetener   Edulcorante utilizado.
     * @param description Descripción del postre.
     */
    public LightDessert(String code, String name, BigDecimal price, String sweetener, String description) {
        super(code, name, price);
        this.sweetener = sweetener;
        this.description = description;
    }

    /** Devuelve el edulcorante utilizado en el postre light. */
    public String getSweetener() {
        return sweetener;
    }

    /** Devuelve la descripción del postre light. */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("LightDessert{code=%s, name=%s, price=%s, sweetener=%s, description=%s}",
                getCode(), getName(), getPrice(), sweetener, description);
    }
}
