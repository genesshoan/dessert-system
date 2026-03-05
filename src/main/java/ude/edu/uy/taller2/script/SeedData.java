package ude.edu.uy.taller2.script;

import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.dto.LightDessertDTO;
import ude.edu.uy.taller2.logic.LogicLayer;
import ude.edu.uy.taller2.server.collection.SaleDecision;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SeedData {
    public static void main(String[] args) throws Exception {
        LogicLayer logic = LogicLayer.getInstance();

        String[] sweeteners = {"Stevia", "Sucralosa", "Aspartamo", "Sacarina"};
        String[] adjectives = {"cremoso", "esponjoso", "crujiente", "suave", "intenso"};

        // 30 postres normales
        for (int i = 1; i <= 30; i++) {
            BigDecimal price = new BigDecimal(50 + i * 10 + ".50");
            logic.createDessert(new DessertDTO(
                    "norm" + i,
                    "Postre normal " + adjectives[i % adjectives.length] + " #" + i,
                    price));
        }

        // 20 postres light
        for (int i = 1; i <= 20; i++) {
            BigDecimal price = new BigDecimal(80 + i * 8 + ".00");
            logic.createDessert(new LightDessertDTO(
                    "light" + i,
                    "Postre light " + adjectives[i % adjectives.length] + " #" + i,
                    price,
                    sweeteners[i % sweeteners.length],
                    "Descripción detallada del postre light número " + i + ", elaborado con ingredientes seleccionados."));
        }

        // 40 ventas, mezcla de estados y fechas
        LocalDate baseDate = LocalDate.of(2026, 1, 1);
        String[] addresses = {
                "Av. 18 de Julio 1234", "Bvar. Artigas 800", "Calle Colón 456",
                "Av. Italia 3000", "Calle Goes 1500", "Av. Millán 900"
        };

        for (int i = 1; i <= 40; i++) {
            LocalDate date = baseDate.plusDays(i);
            logic.createSale(addresses[i % addresses.length] + ", apto " + i, date);

            // Cada venta tiene entre 2 y 5 items distintos
            int itemCount = 2 + (i % 4);
            for (int j = 0; j < itemCount; j++) {
                int dessertIdx = (i + j) % 30 + 1;
                int units = 1 + (j % 5);
                logic.addDessertUnits("norm" + dessertIdx, i, units);
            }

            // Las primeras 25 completadas, las últimas 15 en progreso
            if (i <= 25) {
                logic.finalizeSale(i, SaleDecision.COMPLETE);
            }
        }

        logic.saveData();
        System.out.println("Seed completado — 50 postres, 40 ventas");
    }
}