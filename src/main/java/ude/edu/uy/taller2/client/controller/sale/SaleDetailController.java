package ude.edu.uy.taller2.client.controller.sale;

import ude.edu.uy.taller2.client.controller.BaseController;
import ude.edu.uy.taller2.dto.SaleDTO;
import ude.edu.uy.taller2.dto.SaleItemDTO;
import ude.edu.uy.taller2.exception.*;
import ude.edu.uy.taller2.server.collection.SaleDecision;
import ude.edu.uy.taller2.ui.sale.SaleDetailDialog;

import java.rmi.RemoteException;
import java.util.List;

public class SaleDetailController extends BaseController<SaleDetailDialog> {

    public SaleDetailController(SaleDetailDialog view) {
        super(view);
    }

    /**
     * Lista los items (postres) de una venta.
     *
     * @param id identificador de la venta
     * @return lista de {@link SaleItemDTO}
     */
    public List<SaleItemDTO> getDessertsBySaleId(long id) {
        try {
            return logicLayer.getDessertsBySaleId(id);
        } catch (SaleNotFoundException e) {
            invalidInput(e);
        } catch (RemoteException e) {
            connectionError();
        }

        return List.of();
    }

    /**
     * Agrega unidades de un postre a una venta existente.
     *
     * @param dessertCode código del postre
     * @param saleId      identificador de la venta
     * @param units       unidades a agregar
     * @return {@link SaleDTO} con el estado actualizado de la venta
     */
    public boolean addDessertUnits(String dessertCode, long saleId, int units) {
        try {
            logicLayer.addDessertUnits(dessertCode, saleId, units);
            return true;
        } catch (MaxUnitsExceededException | DessertNotFoundException | SaleNotFoundException | InvalidSaleOperationException
                | RequiredFieldIsEmptyException e) {
            invalidInput(e);
        } catch (RemoteException e) {
            connectionError();
        }

        return false;
    }

    /**
     * Elimina unidades de un postre de una venta.
     *
     * @param dessertCode código del postre
     * @param saleId      identificador de la venta
     * @param units       unidades a eliminar
     */
    public boolean deleteDessertUnits(String dessertCode, long saleId, int units) {
        try {
            logicLayer.deleteDessertUnits(dessertCode, saleId, units);
            return true;
        } catch (InsufficientUnitsException | SaleNotFoundException | DessertNotFoundException | InvalidSaleOperationException e) {
            invalidInput(e);
        } catch (RemoteException e) {
            connectionError();
        }

        return false;
    }

    /**
     * Finaliza o cancela una venta.
     *
     * @param id           identificador de la venta
     * @param saleDecision acción a realizar (COMPLETE/CANCEL)
     */
    public boolean finalizeSale(long id, SaleDecision saleDecision) {
        try {
            logicLayer.finalizeSale(id, saleDecision);
            return true;
        } catch (SaleNotFoundException | InvalidSaleOperationException e) {
            invalidInput(e);
        } catch (RemoteException e) {
            connectionError();
        }

        return false;
    }
}
