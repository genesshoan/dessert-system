package ude.edu.uy.taller2.dto;

import java.awt.*;
import java.io.Serializable;
import java.math.BigDecimal;

public class DessertDTO implements Serializable {
    private String code;
    private String name;
    BigDecimal price;

    public DessertDTO(String code, String name, BigDecimal price){
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

    public BigDecimal getPrice(){
        return price;
    }

    @Override
    public String toString() {
        return String.format("DessertDTO{code=%s, name=%s, price=%s}",
                code, name, price == null ? "0" : price.toPlainString());
    }
}
