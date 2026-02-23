package ude.edu.uy.taller2.domain;

import ude.edu.uy.taller2.dto.SalesSummaryDTO;
import ude.edu.uy.taller2.exception.MaxUnitsExceededException;
import ude.edu.uy.taller2.exception.InvalidSaleOperationException;
import ude.edu.uy.taller2.exception.InsufficientUnitsException;
import ude.edu.uy.taller2.exception.DessertNotFoundException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Representa una venta de postres.
 * <p>
 * Mantiene la información básica de una venta: identificador, fecha, dirección,
 * estado y la lista de items (postres y cantidades). Controla operaciones
 * básicas como añadir o eliminar unidades y obtener resúmenes.
 */
public class Sale implements Serializable {
    private final int MAX_UNITS_PER_SALE = 40;

    private long id;
    private LocalDate date;
    private String address;
    private SaleStatus status;
    private List<SaleItem> saleItems;

    /**
     * Crea una nueva venta con identificador, fecha y dirección.
     * La venta se inicializa en estado IN_PROGRESS y sin items.
     *
     * @param id      Identificador de la venta (puede ser 0 antes de insertar en la colección).
     * @param date    Fecha de la venta.
     * @param address Dirección asociada a la venta.
     */
    public Sale (long id, LocalDate date, String address) {
        this.id = id;
        this.date = date;
        this.address = address;
        this.status = SaleStatus.IN_PROGRESS;
        this.saleItems = new LinkedList<>();
    }

    /** Devuelve el identificador de la venta. */
    public long getId() {
        return id;
    }

    /** Devuelve la fecha de la venta. */
    public LocalDate getDate() {
        return date;
    }

    /** Devuelve la dirección asociada a la venta. */
    public String getAddress() {
        return address;
    }

    /** Devuelve el estado actual de la venta. */
    public SaleStatus getStatus() {
        return status;
    }

    /**
     * Calcula y devuelve el monto total de la venta sumando el total de cada item.
     *
     * @return Monto total de la venta como BigDecimal.
     */
    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;

        for (SaleItem saleItem : saleItems) {
            total = total.add(saleItem.getTotalAmount());
        }

        return total;
    }

    /** Devuelve la lista de items (postres) de la venta. */
    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    /**
     * Devuelve el número total de unidades sumando las cantidades de cada item.
     *
     * @return Número total de unidades en la venta.
     */
    public int getTotalUnits() {
        int total = 0;

        for (SaleItem saleItem : saleItems) {
            total += saleItem.getQuantity();
        }

        return total;
    }
    
    /**
     * Establece el estado de la venta.
     *
     * @param status Nuevo estado para la venta.
     */
    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    /**
     * Establece el identificador de la venta.
     *
     * @param id Nuevo identificador.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Indica si la venta puede ser modificada (está en progreso).
     *
     * @return true si la venta está en progreso y se puede modificar; false en caso contrario.
     */
    public boolean checkModifiable() {
        return status == SaleStatus.IN_PROGRESS;
    }

    /**
     * Añade unidades de un postre a la venta. Si el postre ya existe en la venta
     * incrementa su cantidad; si no, crea un nuevo item.
     *
     * @param dessert Postre a añadir.
     * @param units   Cantidad de unidades a añadir (debe ser > 0).
     * @throws InvalidSaleOperationException Si las unidades no son positivas o la venta no es modificable.
     * @throws MaxUnitsExceededException Si el agregado excede el límite máximo por venta.
     */
    public void addDessertUnits(Dessert dessert, int units) {
        if (units <= 0) {
            throw new InvalidSaleOperationException("Units must be positive");
        }

        if (!checkModifiable()) {
            throw new InvalidSaleOperationException("The sale process is already finished. It cannot be modified");
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
            targetItem.setQuantity(targetItem.getQuantity() + units);
        }
    }

    /**
     * Elimina una cantidad de unidades de un postre identificado por código.
     * Si la cantidad resultante de un item es 0, el item se elimina de la venta.
     *
     * @param code     Código del postre a eliminar.
     * @param quantity Cantidad de unidades a eliminar (debe ser > 0 y <= cantidad actual).
     * @throws InvalidSaleOperationException Si la cantidad no es válida o la venta no es modificable.
     * @throws DessertNotFoundException Si el postre no existe en la venta.
     * @throws InsufficientUnitsException Si se intenta eliminar más unidades de las existentes.
     */
    public void deleteDessertUnits(String code, int quantity){
        if(quantity <= 0){
            throw new InvalidSaleOperationException("The amount entered is invalid");
        }

        if (!checkModifiable()) {
            throw new InvalidSaleOperationException("The sale is not in process");
        }

        SaleItem targetItem = null;

        for (SaleItem item : saleItems){
            if (item.getDessert().getCode().equals(code)){
                targetItem = item;
            }
        }

        if (targetItem == null){
            throw new DessertNotFoundException("The dessert not exist in the sale");
        }

        if (quantity > targetItem.getQuantity()){
            throw new InsufficientUnitsException("No more units can be removed than already exist");
        }

        if(targetItem.getQuantity() - quantity == 0) {
            saleItems.remove(targetItem);
        } else {
            targetItem.setQuantity(targetItem.getQuantity() - quantity);
        }
    }

    /**
     * Obtiene un resumen de ventas (unidades y monto) para un postre concreto dentro de esta venta.
     *
     * @param code Código del postre.
     * @return DTO con el total de unidades y el monto total para el postre especificado.
     */
    public SalesSummaryDTO getSaleSummaryByDessert(String code) {
        int totalUnits = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SaleItem saleItem : saleItems) {
            if(saleItem.getDessert().getCode().equals(code)) {
                totalUnits += saleItem.getQuantity();
                totalAmount = totalAmount.add(
                        saleItem.getTotalAmount());
            }
        }

        return new SalesSummaryDTO(totalUnits, totalAmount);
    }

    /** Devuelve una representación en cadena de la venta con sus campos principales. */
    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", date=" + date +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", totalAmount=" + getTotalAmount() +
                ", totalUnits=" + getTotalUnits() +
                ", saleItems=" + saleItems +
                '}';
    }
}