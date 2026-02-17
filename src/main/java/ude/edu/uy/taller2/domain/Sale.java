package ude.edu.uy.taller2.domain;

import ude.edu.uy.taller2.dto.SalesSummaryDTO;
import ude.edu.uy.taller2.exception.MaxUnitsExceededException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Sale {
    private final int MAX_UNITS_PER_SALE = 40;

    private long id;
    private LocalDate date;
    private String address;
    private SaleStatus status;
    private List<SaleItem> saleItems;

    public Sale (long id, LocalDate date, String address) {
        this.id = id;
        this.date = date;
        this.address = address;
        this.status = SaleStatus.IN_PROGRESS;
        this.saleItems = new LinkedList<>();
    }

    public long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;

        for (SaleItem saleItem : saleItems) {
            total = total.add(saleItem.getTotalAmount());
        }

        return total;
    }

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public int getTotalUnits() {
        int total = 0;

        for (SaleItem saleItem : saleItems) {
            total += saleItem.getQuantity();
        }

        return total;
    }
    
    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public boolean checkModifiable() {
        return status == SaleStatus.IN_PROGRESS;
    }

    public void addDessertUnits(Dessert dessert, int units) {
        if (units < 0) {
            throw new IllegalStateException("Units must be positive");
        }

        if (!checkModifiable()) {
            throw new IllegalStateException("The sale process is already finished. It cannot be modified");
        }

        int totalUnits = getTotalUnits() + units;

        if (totalUnits > 40) {
            throw new MaxUnitsExceededException("A sale cannot exceed 40 dessert units");
        }

        SaleItem targetItem = null;

        for (SaleItem item : saleItems){
            if (item.getDessert().getCode().equals(dessert.getCode())){
                targetItem = item;
            }
        }

        if (targetItem == null) {
            saleItems.add(new SaleItem(units, dessert));
        } else {
            targetItem.setQuantity(totalUnits);
        }
    }

    public void deleteDessertUnits(String code, int quantity){
        if(quantity <= 0){
            throw new IllegalStateException("The amount entered is invalid");
        }

        if (!checkModifiable()) {
            throw new IllegalStateException("The sale is not in process");
        }

        SaleItem targetItem = null;

        for (SaleItem item : saleItems){
            if (item.getDessert().getCode().equals(code)){
                targetItem = item;
            }
        }

        if (targetItem == null){
            throw new IllegalArgumentException("The dessert not exist in the sale");
        }

        if (quantity > targetItem.getQuantity()){
            throw new IllegalArgumentException("No more units can be removed than already exist");
        }

        targetItem.setQuantity(targetItem.getQuantity() - quantity);
    }

    public SalesSummaryDTO getSaleSummaryByDessert(String code) {
        int totalUnits = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SaleItem saleItem : saleItems) {
            totalUnits += saleItem.getQuantity();
            totalAmount = totalAmount.add(
                    saleItem.getTotalAmount());
        }

        return new SalesSummaryDTO(totalUnits, totalAmount);
    }
}