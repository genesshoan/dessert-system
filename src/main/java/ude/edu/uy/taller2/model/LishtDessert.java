package ude.edu.uy.taller2.model;

import java.math.BigDecimal;

public class LishtDessert extends Dessert{
    private String sweetener;
    private String description;

    public LishtDessert(String code, String name, BigDecimal price, String sweetener, String description) {
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
}
