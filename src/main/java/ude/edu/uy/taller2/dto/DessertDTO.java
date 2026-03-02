package ude.edu.uy.taller2.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Data Transfer Object that represents a dessert exposed by the logic layer.
 * Contains only the data required by clients: code, name and price.
 */
public class DessertDTO implements Serializable {
    private String code;
    private String name;
    BigDecimal price;

    /**
     * Create a DessertDTO.
     *
     * @param code  Unique dessert code
     * @param name  Dessert name
     * @param price Dessert price
     */
    public DessertDTO(String code, String name, BigDecimal price){
        this.code = code;
        this.name = name;
        this.price = price;
    }

    /** Returns the unique dessert code. */
    public String getCode(){
        return code;
    }

    /** Returns the dessert name. */
    public String getName(){
        return name;
    }

    /** Returns the dessert price. */
    public BigDecimal getPrice(){
        return price;
    }

    @Override
    public String toString() {
        return String.format("DessertDTO{code=%s, name=%s, price=%s}",
                code, name, price == null ? "0" : price.toPlainString());
    }
}
