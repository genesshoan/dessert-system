package ude.edu.uy.taller2;

import ude.edu.uy.taller2.collection.SaleDecision;
import ude.edu.uy.taller2.collection.SaleFilter;
import ude.edu.uy.taller2.collection.Sales;
import ude.edu.uy.taller2.domain.Dessert;
import ude.edu.uy.taller2.domain.Sale;
import ude.edu.uy.taller2.dto.SalesSummaryDTO;
import ude.edu.uy.taller2.exception.MaxUnitsExceededException;
import ude.edu.uy.taller2.exception.InsufficientUnitsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Programa de prueba simple para verificar operaciones básicas sobre ventas.
 * Inserta ventas y muestra consultas de ejemplo con salida formateada para facilitar su lectura.
 */
public class TestSales {

    public static void main(String[] args) {
        Sales sales = new Sales();

        // Crear ventas iniciales
        Sale sale1 = new Sale(0, LocalDate.now(), "Montevideo");
        Sale sale2 = new Sale(0, LocalDate.now(), "Colonia del Sacramento");
        Sale sale3 = new Sale(0, LocalDate.now(), "Rosario");
        Sale sale4 = new Sale(0, LocalDate.now(), "Cardona");
        Sale sale5 = new Sale(0, LocalDate.now(), "Conchillas");

        TestHelpers.printHeader("Insertar ventas");

        sales.insert(sale1);
        sales.insert(sale2);
        sales.insert(sale3);
        sales.insert(sale4);
        sales.insert(sale5);

        TestHelpers.printSub("Ventas actuales (id y dirección):");
        sales.getByStatus(SaleFilter.ALL).forEach((s) -> System.out.println(s.getId() + " " + s.getAddress()));

        TestHelpers.printHeader("Comprobaciones básicas");

        TestHelpers.printSub("Exists by id");
        System.out.println("Deberia existir con id 1: " + sales.existsById(1));
        System.out.println("Deberia existir con id 2: " + sales.existsById(2));
        System.out.println("NO deberia existir con id 16: " + sales.existsById(16));

        TestHelpers.printSub("Find");
        System.out.println("Deberia encontrarlo con id 2: " + (sales.find(2) != null));
        System.out.println("NO deberia encontrarlo con id 7: " + (sales.find(7) != null));

        TestHelpers.printHeader("Probar finalización y reciclado de IDs");
        System.out.println("Cancelamos la venta con id 5 (CANCEL)");
        sales.finalizeSale(5, SaleDecision.CANCEL);
        System.out.println("Insertamos una nueva venta (debe tomar id siguiente)");
        sales.insert(new Sale(0, LocalDate.now(), "Artigas"));
        sales.getByStatus(SaleFilter.ALL).forEach((s) -> System.out.println(s.getId() + " " + s.getAddress()));

        TestHelpers.printHeader("Probar completar venta vacía y no vacía");
        System.out.println("Completamos la venta con id 2 (vacía) -> debe eliminarse");
        sales.finalizeSale(2, SaleDecision.COMPLETE);

        // Crear postres de prueba
        Dessert flan = new Dessert("flan_con_dulce123", "Flan con dulce", BigDecimal.valueOf(123.5));
        Dessert tiramisu = new Dessert("tiramisu01", "Tiramisu", BigDecimal.valueOf(200.0));

        // Añadir items a la venta 1
        System.out.println("Añadimos items a la venta 1");
        sales.addDessertUnits(1, flan, 3);
        sales.addDessertUnits(1, tiramisu, 2);

        TestHelpers.printSub("¿Se borró la venta con id 2? " + !sales.existsById(2));
        TestHelpers.printSub("¿Se borró la venta con id 1? " + !sales.existsById(1));

        TestHelpers.printSub("Ventas actuales (id y dirección):");
        sales.getByStatus(SaleFilter.ALL).forEach((s) -> System.out.println(s.getId() + " " + s.getAddress()));

        TestHelpers.printSub("Detalle de la venta 1:");
        List<?> itemsSale1 = sales.getDessertsBySaleId(1);
        itemsSale1.forEach(System.out::println);

        TestHelpers.printHeader("Operaciones sobre items");

        // Eliminar una unidad de tiramisu en la venta 1
        System.out.println("Eliminamos 1 unidad de tiramisu en venta 1");
        sales.deleteDessertUnits(1, "tiramisu01", 1);
        System.out.println("Estado de items en venta 1 tras eliminación:");
        sales.getDessertsBySaleId(1).forEach(System.out::println);

        // Intentar eliminar más unidades de las que existen (debe lanzar excepción)
        System.out.println("Intentamos eliminar demasiadas unidades (esperamos InsufficientUnitsException)");
        try {
            sales.deleteDessertUnits(1, "tiramisu01", 10);
        } catch (InsufficientUnitsException e) {
            System.out.println("Capturada InsufficientUnitsException: " + e.getMessage());
        }

        // Intentar exceder el máximo por venta
        System.out.println("Intentamos exceder el máximo de unidades por venta (esperamos MaxUnitsExceededException)");
        try {
            sales.addDessertUnits(1, flan, 100);
        } catch (MaxUnitsExceededException e) {
            System.out.println("Capturada MaxUnitsExceededException: " + e.getMessage());
        }

        TestHelpers.printHeader("Resumen por postre en la fecha de hoy");
        SalesSummaryDTO summary = sales.getSalesByDessert("flan_con_dulce123", LocalDate.now());
        System.out.println("Total unidades (flan_con_dulce123): " + summary.getTotalUnits());
        System.out.println("Monto total (flan_con_dulce123): " + summary.getTotalAmount());
        TestHelpers.printSub("Completamos la venta 1");
        sales.finalizeSale(1, SaleDecision.COMPLETE);
        summary = sales.getSalesByDessert("flan_con_dulce123", LocalDate.now());
        System.out.println("Total unidades (flan_con_dulce123): " + summary.getTotalUnits());
        System.out.println("Monto total (flan_con_dulce123): " + summary.getTotalAmount());

        TestHelpers.printHeader("Información final de ventas");
        sales.getByStatus(SaleFilter.ALL).forEach(System.out::println);

        TestHelpers.printHeader("Fin del test");
    }
}
