package ude.edu.uy.taller2.collection;

import ude.edu.uy.taller2.domain.Dessert;
import ude.edu.uy.taller2.domain.Sale;
import ude.edu.uy.taller2.domain.SaleItem;
import ude.edu.uy.taller2.domain.SaleStatus;
import ude.edu.uy.taller2.dto.SalesSummaryDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Sales {
    private final LinkedList<Sale> sales;

    public Sales() {
        sales = new LinkedList<>();
    }

    public boolean existsById(long id) {
        return sales.stream()
                .anyMatch((s) -> s.getId() == id);
    }

    public void insert(Sale sale){
        sales.add(sale);
    }

    public Sale find(long id) {
        return sales.stream()
                .filter((s) -> s.getId() == id)
                .findFirst()
                    .orElse(null);
    }

    public void addDessertUnits(long id, Dessert dessert, int units) {
        find(id).addDessertUnits(dessert, units);
    }

    public void deleteDessertUnits(long saleId, String code, int quantity) {
        Sale sale = find((saleId));

        sale.deleteDessertUnits(code, quantity);
    }

    public void finalizeSale(long saleId, SaleDecision action) {
        Sale sale = find(saleId);

        if (sale == null) {
            throw new IllegalArgumentException
                    ("There is no sales process with the provided identifier");
        }

        if (sale.getStatus() == SaleStatus.COMPLETED) {
            throw new IllegalStateException
                    ("The sale has already been completed.");
        }

        if (sale.getSaleItems().isEmpty()) {
            sales.remove(sale);
            return;
        }

        switch (action) {
            case COMPLETE:
                sale.setStatus(SaleStatus.COMPLETED);
                break;

            case CANCEL:
                sales.remove(sale);
                break;
        }
    }

    public List<Sale> getByStatus(SaleFilter saleFilter) {
        return sales.stream()
                .filter((s) -> switch (saleFilter) {
                        case PENDING -> s.getStatus() == SaleStatus.IN_PROGRESS;
                        case FINISHED -> s.getStatus() == SaleStatus.COMPLETED;
                        case ALL -> true;
                })
                .toList();
    }

    public List<SaleItem> getDessertsBySaleId(long id) {
        return find(id).getSaleItems();
    }

    public SalesSummaryDTO getSalesByDessert(String code, LocalDate date) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalUnits = 0;

        for (Sale sale : sales) {
            if(sale.getStatus() == SaleStatus.COMPLETED
                && sale.getDate().equals(date)) {
                SalesSummaryDTO salesSummaryDTO = sale.getSaleSummaryByDessert(code);
                totalAmount = totalAmount.add(
                        salesSummaryDTO.getTotalAmount());
                totalUnits += salesSummaryDTO.getTotalUnits();
            }
        }

        return new SalesSummaryDTO(totalUnits, totalAmount);
    }
}
