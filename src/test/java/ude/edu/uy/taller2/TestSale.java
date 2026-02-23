package ude.edu.uy.taller2;

import ude.edu.uy.taller2.domain.Dessert;
import ude.edu.uy.taller2.domain.Sale;
import ude.edu.uy.taller2.domain.SaleStatus;
import ude.edu.uy.taller2.exception.DessertNotFoundException;
import ude.edu.uy.taller2.exception.InvalidSaleOperationException;
import ude.edu.uy.taller2.exception.InsufficientUnitsException;
import ude.edu.uy.taller2.exception.MaxUnitsExceededException;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Test manual para la clase Sale (ejecución vía main, sin JUnit).
 * Cubre añadir/eliminar unidades, excepciones personalizadas y resúmenes.
 */
public class TestSale {
    public static void main(String[] args) {
        // Creo las instancias necesarias
        Sale sale = new Sale(0, LocalDate.now(), "Calle Juan Zorrilla de San Martin, esq. P. Pablo Davit S/N");
        Dessert flan = new Dessert("flan12", "Flan con dulce de leche", BigDecimal.valueOf(190.3));
        Dessert alfajor = new Dessert("alfajor323", "Alfajor de maicena", BigDecimal.valueOf(100));

        TestHelpers.printHeader("Anadir desserts");

        TestHelpers.printSub("Anado 3 unidades de flan");
        sale.addDessertUnits(flan, 3);
        System.out.println(sale.getSaleItems());
        TestHelpers.printSub("Anado nuevamente dos unidades del mismo postre");
        sale.addDessertUnits(flan, 2);
        System.out.println(sale.getSaleItems());

        TestHelpers.printSub("Anado otro postre a la venta");
        sale.addDessertUnits(alfajor, 10);
        System.out.println(sale.getSaleItems());

        TestHelpers.printHeader("Resumen y totales");
        System.out.println("Total unidades en la venta: " + sale.getTotalUnits());
        System.out.println("Monto total de la venta: " + sale.getTotalAmount());
        System.out.println("Resumen por flan: " + sale.getSaleSummaryByDessert(flan.getCode()));

        TestHelpers.printHeader("Excedo la cantidad de postres soportada");
        try {
            sale.addDessertUnits(alfajor, 50); // debería lanzar MaxUnitsExceededException
        } catch (MaxUnitsExceededException e) {
            System.out.println("Capturada MaxUnitsExceededException: " + e.getMessage());
            System.out.println("Estado actual de items: " + sale.getSaleItems());
        }

        TestHelpers.printHeader("Eliminar unidades de postres");
        TestHelpers.printSub("Elimino parcialmente las unidades de flan");
        TestHelpers.printSub("Antes");
        System.out.println(sale.getSaleItems());
        sale.deleteDessertUnits(flan.getCode(), 2);
        TestHelpers.printSub("Despues");
        System.out.println(sale.getSaleItems());

        TestHelpers.printSub("Elimino por completo las unidades de flan (debe eliminar el item)");
        sale.deleteDessertUnits(flan.getCode(), 3);
        System.out.println(sale.getSaleItems());

        TestHelpers.printHeader("Intentos inválidos y manejo de errores");
        TestHelpers.printSub("Eliminar postre no existente -> DessertNotFoundException");
        try {
            sale.deleteDessertUnits("no_existe", 1);
        } catch (DessertNotFoundException e) {
            System.out.println("Capturada DessertNotFoundException: " + e.getMessage());
        }

        TestHelpers.printSub("Eliminar más unidades de las que existen -> InsufficientUnitsException");
        // preparar: añadir 1 unidad de tiramisu y luego intentar quitar 5
        Dessert tiramisu = new Dessert("tira1", "Tiramisu", BigDecimal.valueOf(200));
        sale.addDessertUnits(tiramisu, 1);
        try {
            sale.deleteDessertUnits(tiramisu.getCode(), 5);
        } catch (InsufficientUnitsException e) {
            System.out.println("Capturada InsufficientUnitsException: " + e.getMessage());
        }

        TestHelpers.printHeader("Modificar una venta completada");
        try {
            sale.setStatus(SaleStatus.COMPLETED);
            sale.addDessertUnits(flan, 3);
        } catch (InvalidSaleOperationException e) {
            System.out.println("Capturada InvalidSaleOperationException: " + e.getMessage());
            System.out.println(sale.getSaleItems());
        }

        TestHelpers.printHeader("Estado final de la venta");
        System.out.println(sale);

        TestHelpers.printHeader("Fin del test");
    }
}
