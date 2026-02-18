package ude.edu.uy.taller2.collection;

import ude.edu.uy.taller2.domain.Dessert;

import java.util.List;
import java.util.TreeMap;

public class Desserts {
    private final TreeMap<String, Dessert> desserts;

    public Desserts() {
        desserts = new TreeMap<>();
    }

    public boolean member(String code) {
        return desserts.containsKey(code);
    }

    public void insert(Dessert dessert) {
        desserts.put(dessert.getCode(), dessert);
    }

    public Dessert find(String code) {
        return desserts.get(code);
    }

    public List<Dessert> getDesserts() {
        return List.copyOf(desserts.values());
    }
}
