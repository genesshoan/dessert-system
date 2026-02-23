package ude.edu.uy.taller2;

import ude.edu.uy.taller2.collection.Desserts;
import ude.edu.uy.taller2.domain.Dessert;
import ude.edu.uy.taller2.domain.LightDessert;

import java.math.BigDecimal;

/**
 * Programa de prueba simple para verificar operaciones básicas del catálogo de postres.
 * Contiene un main que inserta postres y muestra consultas simples.
 */
public class TestDesserts {
    public static void main(String[] args) {
        Desserts desserts = new Desserts();

        Dessert dessert1 = new Dessert("flan32", "flancondulce", BigDecimal.valueOf(230));
        Dessert dessert2 = new Dessert("chocotorta342", "chocotorta", BigDecimal.valueOf(2344));
        Dessert dessert3 = new Dessert("cheesecacke", "cheese_cacke", BigDecimal.valueOf(3442));
        Dessert dessert4 = new LightDessert("light3434", "lemonpie", BigDecimal.valueOf(3234.5), "Edulcorante", "ni idea");

        TestHelpers.printHeader("Catálogo de postres - Test");

        TestHelpers.printSub("Insertar postres");
        desserts.insert(dessert1);
        System.out.println("Postre cargado: " + dessert1.getCode() + " - " + dessert1.getName());
        desserts.insert(dessert2);
        System.out.println("Postre cargado: " + dessert2.getCode() + " - " + dessert2.getName());
        desserts.insert(dessert3);
        System.out.println("Postre cargado: " + dessert3.getCode() + " - " + dessert3.getName());
        desserts.insert(dessert4);
        System.out.println("Postre cargado: " + dessert4.getCode() + " - " + dessert4.getName());

        TestHelpers.printSub("Comprobación de membresía");
        System.out.printf("Existe %s? %b%n", "flan32", desserts.member("flan32"));
        System.out.printf("Existe %s? %b%n", "flan3", desserts.member("flan3"));
        System.out.printf("Existe %s? %b%n", "chocotorta342", desserts.member("chocotorta342"));
        System.out.printf("Existe %s? %b%n", "light3434", desserts.member("light3434"));

        TestHelpers.printSub("Búsquedas por código");
        System.out.printf("Codigo %s, deberia de encontrarlo: %b%n", "flan32", desserts.find("flan32") != null);
        System.out.printf("Codigo %s, deberia de encontrarlo: %b%n", "light3434", desserts.find("light3434") != null);
        System.out.printf("Codigo %s, no deberia de encontrarlo: %b%n", "hamburguer", desserts.find("hamburguer") != null);

        TestHelpers.printSub("Listado de postres (toString de cada postre)");
        desserts.getDesserts().forEach(System.out::println);

        TestHelpers.printHeader("Fin del test de catálogo");
    }
}
