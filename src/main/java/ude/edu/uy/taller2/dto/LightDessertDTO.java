package ude.edu.uy.taller2.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for light desserts that extends {@link DessertDTO} adding sweetener and description.
 */
public class LightDessertDTO extends DessertDTO implements Serializable {
    private String sweetener;
    private String description;

    /**
     * Create a LightDessertDTO.
     *
     * @param code        dessert code
     * @param name        dessert name
     * @param price       dessert price
     * @param sweetener   sweetener used
     * @param description description text
     */
    public LightDessertDTO(String code, String name, BigDecimal price, String sweetener, String description) {
        super(code, name, price);
        this.sweetener = sweetener;
        this.description = description;
    }

    /** Returns the sweetener used in the light dessert. */
    public String getSweetener() {
        return sweetener;
    }

    /** Returns the description of the light dessert. */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("LightDessertDTO{%s, sweetener=%s, description=%s}",
                super.toString().replaceFirst("DessertDTO", ""),
                sweetener, description);
    }
}