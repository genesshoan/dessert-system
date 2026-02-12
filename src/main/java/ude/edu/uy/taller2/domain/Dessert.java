package ude.edu.uy.taller2.domain;

import java.math.BigDecimal;

public class Dessert {
    private String code;
    private String name;
    private BigDecimal price;

    public Dessert(String code, String name, BigDecimal price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public  BigDecimal getPrice() {
        return  price;
    }

    @Override
    public String toString() {
        return "Dessert{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
