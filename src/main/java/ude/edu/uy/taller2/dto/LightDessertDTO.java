package ude.edu.uy.taller2.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class LightDessertDTO extends DessertDTO implements Serializable {
    private String sweetener;
    private String description;

    public LightDessertDTO(String code, String name, BigDecimal price, String sweetener, String description) {
        super(code, name, price);
        this.sweetener = sweetener;
        this.description = description;
    }

    public String getSweetener() {
        return sweetener;
    }

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