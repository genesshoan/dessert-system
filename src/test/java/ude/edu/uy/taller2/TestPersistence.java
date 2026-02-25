package ude.edu.uy.taller2;

import ude.edu.uy.taller2.collection.Desserts;
import ude.edu.uy.taller2.collection.SaleDecision;
import ude.edu.uy.taller2.collection.SaleFilter;
import ude.edu.uy.taller2.collection.Sales;
import ude.edu.uy.taller2.domain.Dessert;
import ude.edu.uy.taller2.domain.LightDessert;
import ude.edu.uy.taller2.domain.Sale;
import ude.edu.uy.taller2.exception.AppDataPersistenceException;
import ude.edu.uy.taller2.persistence.AppData;
import ude.edu.uy.taller2.persistence.Persistence;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestPersistence {
    public static void main(String[] args) throws Exception {
        // Creamos las colecciones
        Desserts desserts = new Desserts();
        Sales sales = new Sales();

        // Creamos los postres
        Dessert dessert1 = new Dessert("flan32", "flancondulce", BigDecimal.valueOf(230));
        Dessert dessert2 = new Dessert("chocotorta342", "chocotorta", BigDecimal.valueOf(2344));
        Dessert dessert3 = new Dessert("cheesecacke", "cheese_cacke", BigDecimal.valueOf(3442));
        Dessert dessert4 = new LightDessert("light3434", "lemonpie", BigDecimal.valueOf(3234.5), "Edulcorante", "ni idea");

        // Creamos las ventas
        Sale sale1 = new Sale(0, LocalDate.now(), "Montevideo");
        Sale sale2 = new Sale(0, LocalDate.now(), "Colonia del Sacramento");
        Sale sale3 = new Sale(0, LocalDate.now(), "Rosario");
        Sale sale4 = new Sale(0, LocalDate.now(), "Cardona");
        Sale sale5 = new Sale(0, LocalDate.now(), "Conchillas");

        // Insertamos los postres
        desserts.insert(dessert1);
        desserts.insert(dessert2);
        desserts.insert(dessert3);
        desserts.insert(dessert4);

        //Insertamos las ventas
        sales.insert(sale1);
        sales.insert(sale2);
        sales.insert(sale3);
        sales.insert(sale4);
        sales.insert(sale5);

        // Añadimos detalles al input
        sales.addDessertUnits(1, dessert1, 20);
        sales.addDessertUnits(2, dessert3, 3);
        sales.addDessertUnits(2, dessert4, 1);
        sales.finalizeSale(2, SaleDecision.COMPLETE);

        TestHelpers.printHeader("Test de persistencia de datos");

        TestHelpers.printSub("Desserts a guardar");
        desserts.getDesserts().forEach(System.out::println);

        TestHelpers.printSub("Sales a guardar");
        sales.getByStatus(SaleFilter.ALL).forEach(System.out::println);


        Desserts loadedDesserts;
        Sales loadedSales;

        // Respaldamos y levantamos los datos
        try {
            Persistence.save(new AppData(desserts, sales));
            AppData appData = Persistence.load();
            loadedSales = appData.getSales();
            loadedDesserts = appData.getDesserts();

            // Mostramos los resultados
            TestHelpers.printSub("Desserts levantados");
            loadedDesserts.getDesserts().forEach(System.out::println);

            TestHelpers.printSub("Sales levantadas");
            loadedSales.getByStatus(SaleFilter.ALL).forEach(System.out::println);
        } catch (AppDataPersistenceException e) {
            System.out.println("Error al respaldar");
        }
    }
}
