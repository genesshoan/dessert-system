package ude.edu.uy.taller2;

import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.exception.*;
import ude.edu.uy.taller2.logic.LogicLayer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Test manual para la capa lógica expuesto por `LogicLayer`.
 * Ejecutable vía `main` (sin JUnit) para uso en el entorno de la universidad.
 * Muestra operaciones básicas: creación de postres, listado y creación de venta.
 */
public class TestLogicLayer {
    public static void main(String[] args) throws Exception {
        TestHelpers.printHeader("Test LogicLayer");

        LogicLayer logic = LogicLayer.getInstance();

        TestHelpers.printSub("Creando postres de ejemplo");
        try {
            logic.createDessert(new DessertDTO("d1", "Flan de coco", BigDecimal.valueOf(120.5)));
            logic.createDessert(new DessertDTO("d2", "Tiramisu", BigDecimal.valueOf(250)));
        } catch (DessertAlreadyExistsException | InvalidAmountException | RequiredFieldIsEmptyException e) {
            System.out.println("Error al crear postres: " + e.getMessage());
        }

        TestHelpers.printSub("Listado de postres");
        List<DessertDTO> desserts = logic.getAllDesserts();
        desserts.forEach(System.out::println);

        TestHelpers.printSub("Creando una venta");
        try {
            logic.createSale("Calle Falsa 123", LocalDate.now());
            TestHelpers.printSub("Venta creada exitosamente (se asume id = 1 para esta prueba)");
        } catch (RequiredFieldIsEmptyException e) {
            System.out.println("Error al crear venta: " + e.getMessage());
        }

        TestHelpers.printHeader("Pruebas de agregar y borrar unidades de postres en la venta (LogicLayer)");

        long saleId = 1;

        TestHelpers.printSub("Agregar 3 unidades de d1 a la venta 1");
        try {
            ude.edu.uy.taller2.dto.SaleDTO saleDTO = logic.addDessertUnits("d1", saleId, 3);
            System.out.println("Después de agregar: " + saleDTO);
        } catch (RequiredFieldIsEmptyException | DessertNotFoundException | SaleNotFoundException |
                InvalidSaleOperationException | MaxUnitsExceededException e) {
            System.out.println("Error al agregar unidades: " + e.getMessage());
        }

        TestHelpers.printSub("Intento de agregar demasiadas unidades (debe lanzar MaxUnitsExceededException)");
        try {
            logic.addDessertUnits("d1", saleId, 100);
        } catch (MaxUnitsExceededException e) {
            System.out.println("Capturada MaxUnitsExceededException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Otra excepción capturada: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        TestHelpers.printSub("Borrar 2 unidades de d1 en la venta 1");
        try {
            logic.deleteDessertUnits("d1", saleId, 2);
            System.out.println("Eliminadas 2 unidades de d1 en la venta " + saleId);
        } catch (SaleNotFoundException | InvalidSaleOperationException | DessertNotFoundException | InsufficientUnitsException e) {
            System.out.println("Error al borrar unidades: " + e.getMessage());
        }

        TestHelpers.printSub("Intento de borrar unidades de postre inexistente (debe lanzar DessertNotFoundException)");
        try {
            logic.deleteDessertUnits("no_existe", saleId, 1);
        } catch (DessertNotFoundException e) {
            System.out.println("Capturada DessertNotFoundException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Otra excepción capturada: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        TestHelpers.printHeader("Fin Test LogicLayer");
    }
}
