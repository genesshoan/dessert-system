package ude.edu.uy.taller2;

import ude.edu.uy.taller2.server.collection.SaleDecision;
import ude.edu.uy.taller2.server.collection.SaleFilter;
import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.dto.SaleItemDTO;
import ude.edu.uy.taller2.dto.SalesSummaryDTO;
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

        TestHelpers.printSub("Obtener postre por código (d1)");
        try {
            DessertDTO d1 = logic.getDessertByCode("d1");
            System.out.println("getDessertByCode(d1) => " + d1);
        } catch (RequiredFieldIsEmptyException | ude.edu.uy.taller2.exception.DessertNotFoundException e) {
            System.out.println("Error al obtener postre: " + e.getMessage());
        }

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

            TestHelpers.printSub("Items de la venta 1 (getDessertsBySaleId)");
            try {
                List<SaleItemDTO> items = logic.getDessertsBySaleId(saleId);
                items.forEach(System.out::println);
            } catch (SaleNotFoundException e) {
                System.out.println("Error al listar items de la venta: " + e.getMessage());
            }

        } catch (RequiredFieldIsEmptyException | ude.edu.uy.taller2.exception.DessertNotFoundException | ude.edu.uy.taller2.exception.SaleNotFoundException |
                ude.edu.uy.taller2.exception.InvalidSaleOperationException | ude.edu.uy.taller2.exception.MaxUnitsExceededException e) {
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

            TestHelpers.printSub("Items de la venta 1 después de borrar unidades");
            List<SaleItemDTO> itemsAfterDelete = logic.getDessertsBySaleId(saleId);
            itemsAfterDelete.forEach(System.out::println);

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

        TestHelpers.printHeader("Finalizar y cancelar compra");

        try {
            logic.createSale("Calle Falsa 123", LocalDate.now());
            logic.createSale("Calle Falsa 123", LocalDate.now());
            logic.createSale("Calle Falsa 123", LocalDate.now());
            logic.createSale("Calle Falsa 123", LocalDate.now());
            logic.createSale("Calle Falsa 123", LocalDate.now());

            logic.addDessertUnits("d1", 2, 13);
            logic.addDessertUnits("d1", 4, 13);
        } catch (RequiredFieldIsEmptyException e) {
            System.out.println("Error al crear venta: " + e.getMessage());
        }

        TestHelpers.printSub("Completar venta con postres y sin postres");

        logic.finalizeSale(2, SaleDecision.COMPLETE);
        logic.finalizeSale(3, SaleDecision.COMPLETE);
        logic.finalizeSale(4, SaleDecision.CANCEL);
        logic.getSalesByStatus(SaleFilter.ALL).forEach(System.out::println);

        TestHelpers.printSub("Resumen de ventas por postre (d1)");
        try {
            SalesSummaryDTO summary = logic.getSalesByDessert("d1", LocalDate.now());
            System.out.println("SalesSummary for d1 => " + summary);
        } catch (RequiredFieldIsEmptyException | ude.edu.uy.taller2.exception.DessertNotFoundException e) {
            System.out.println("Error al obtener resumen de ventas: " + e.getMessage());
        }

        TestHelpers.printHeader("Pruebas adicionales: inputs inválidos, entidades faltantes y persistencia");

        TestHelpers.printSub("Crear venta con dirección vacía (debe lanzar RequiredFieldIsEmptyException)");
        try {
            logic.createSale("", LocalDate.now());
        } catch (RequiredFieldIsEmptyException e) {
            System.out.println("Capturada RequiredFieldIsEmptyException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Otra excepción capturada: " + e.getClass().getSimpleName());
        }

        TestHelpers.printSub("Obtener postre con código vacío (debe lanzar RequiredFieldIsEmptyException)");
        try {
            logic.getDessertByCode("");
        } catch (RequiredFieldIsEmptyException e) {
            System.out.println("Capturada RequiredFieldIsEmptyException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Otra excepción capturada: " + e.getClass().getSimpleName());
        }

        TestHelpers.printSub("Obtener postre inexistente (debe lanzar DessertNotFoundException)");
        try {
            logic.getDessertByCode("no-such-code");
        } catch (ude.edu.uy.taller2.exception.DessertNotFoundException e) {
            System.out.println("Capturada DessertNotFoundException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Otra excepción capturada: " + e.getClass().getSimpleName());
        }

        TestHelpers.printSub("Agregar unidades a venta inexistente (debe lanzar SaleNotFoundException)");
        try {
            logic.addDessertUnits("d1", 9999, 1);
        } catch (SaleNotFoundException e) {
            System.out.println("Capturada SaleNotFoundException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Otra excepción capturada: " + e.getClass().getSimpleName());
        }

        TestHelpers.printSub("Finalizar venta inexistente (debe lanzar SaleNotFoundException)");
        try {
            logic.finalizeSale(9999, SaleDecision.COMPLETE);
        } catch (SaleNotFoundException e) {
            System.out.println("Capturada SaleNotFoundException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Otra excepción capturada: " + e.getClass().getSimpleName());
        }

        TestHelpers.printSub("Probar saveData() y loadData() (persistencia)");
        try {
            logic.saveData();
            logic.loadData();
            System.out.println("Persistencia: saveData/loadData ejecutadas correctamente");
        } catch (AppDataPersistenceException e) {
            System.out.println("Error de persistencia: " + e.getMessage());
        }

        TestHelpers.printHeader("Fin Test LogicLayer");
    }
}
