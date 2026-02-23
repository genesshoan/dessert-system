package ude.edu.uy.taller2.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Representa un postre con código, nombre y precio.
 */
public class Dessert implements Serializable {
    private String code;
    private String name;
    private BigDecimal price;

    /**
     * Crea un postre con código, nombre y precio.
     *
     * @param code  Código único del postre.
     * @param name  Nombre del postre.
     * @param price Precio del postre.
     */
    public Dessert(String code, String name, BigDecimal price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    /** Devuelve el código único del postre. */
    public String getCode() {
        return code;
    }

    /** Devuelve el nombre del postre. */
    public String getName() {
        return name;
    }

    /** Devuelve el precio del postre. */
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
