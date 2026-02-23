package ude.edu.uy.taller2.collection;

import ude.edu.uy.taller2.domain.Dessert;
import ude.edu.uy.taller2.domain.Sale;
import ude.edu.uy.taller2.domain.SaleItem;
import ude.edu.uy.taller2.domain.SaleStatus;
import ude.edu.uy.taller2.dto.SalesSummaryDTO;
import ude.edu.uy.taller2.exception.SaleNotFoundException;
import ude.edu.uy.taller2.exception.InvalidSaleOperationException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Gestión en memoria de múltiples ventas. Proporciona operaciones para insertar,
 * buscar, modificar y finalizar ventas.
 */
public class Sales implements Serializable {
    private final LinkedList<Sale> sales;
    private long lastId = 0;

    /**
     * Crea una nueva colección vacía de ventas.
     */
    public Sales() {
        sales = new LinkedList<>();
    }

    /**
     * Indica si existe una venta con el identificador dado.
     *
     * @param id Identificador a consultar.
     * @return true si existe una venta con el id; false en caso contrario.
     */
    public boolean existsById(long id) {
        return sales.stream()
                .anyMatch((s) -> s.getId() == id);
    }

    /**
     * Inserta una venta en la colección y le asigna un nuevo identificador.
     *
     * @param sale Venta a insertar.
     */
    public void insert(Sale sale){
        sale.setId(++lastId);
        sales.add(sale);
    }

    /**
     * Busca y devuelve la venta con el identificador especificado.
     *
     * @param id Identificador de la venta.
     * @return La venta si existe; null si no se encuentra.
     */
    public Sale find(long id) {
        return sales.stream()
                .filter((s) -> s.getId() == id)
                .findFirst()
                    .orElse(null);
    }

    /**
     * Añade unidades de un postre a la venta indicada delegando en la entidad Sale.
     *
     * @param id      Identificador de la venta.
     * @param dessert Postre a añadir.
     * @param units   Cantidad de unidades a añadir.
     */
    public void addDessertUnits(long id, Dessert dessert, int units) {
        find(id).addDessertUnits(dessert, units);
    }

    /**
     * Elimina unidades de un postre en una venta especificada.
     *
     * @param saleId  Identificador de la venta.
     * @param code    Código del postre.
     * @param quantity Cantidad a eliminar.
     */
    public void deleteDessertUnits(long saleId, String code, int quantity) {
        Sale sale = find((saleId));

        sale.deleteDessertUnits(code, quantity);
    }

    /**
     * Finaliza o cancela una venta según la decisión proporcionada.
     *
     * @param saleId Identificador de la venta.
     * @param action Acción a ejecutar (COMPLETE para finalizar, CANCEL para eliminar).
     * @throws SaleNotFoundException Si no existe la venta con el id dado.
     * @throws InvalidSaleOperationException    Si la venta ya fue completada.
     */
    public void finalizeSale(long saleId, SaleDecision action) {
        Sale sale = find(saleId);

        if (sale == null) {
            throw new SaleNotFoundException
                    ("There is no sales process with the provided identifier");
        }

        if (sale.getStatus() == SaleStatus.COMPLETED) {
            throw new InvalidSaleOperationException
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

    /**
     * Devuelve una lista de ventas filtrada por estado según el filtro provisto.
     *
     * @param saleFilter Filtro a aplicar (ALL, PENDING, FINISHED).
     * @return Lista de ventas que cumplen el filtro.
     */
    public List<Sale> getByStatus(SaleFilter saleFilter) {
        return sales.stream()
                .filter((s) -> switch (saleFilter) {
                        case PENDING -> s.getStatus() == SaleStatus.IN_PROGRESS;
                        case FINISHED -> s.getStatus() == SaleStatus.COMPLETED;
                        case ALL -> true;
                })
                .toList();
    }

    /** Devuelve la lista de items de una venta por su identificador. */
    public List<SaleItem> getDessertsBySaleId(long id) {
        return find(id).getSaleItems();
    }

    /**
     * Calcula el total de unidades y monto para un postre en todas las ventas
     * completadas en una fecha dada.
     *
     * @param code Código del postre.
     * @param date Fecha para filtrar ventas completadas.
     * @return DTO con el total de unidades y el monto acumulado.
     */
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
