package ude.edu.uy.taller2.logic;

import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.dto.SaleDTO;
import ude.edu.uy.taller2.exception.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface ILogicLayer extends Remote {
    void createDessert(DessertDTO dessertDTO) throws RemoteException, RequiredFieldIsEmptyException,
            DessertAlreadyExistsException, InvalidAmountException;
    List<DessertDTO> getAllDesserts() throws RemoteException;
    DessertDTO getDessertByCode(String code) throws RemoteException, RequiredFieldIsEmptyException,
            DessertNotFoundException;
    void createSale(String address, LocalDate date) throws RemoteException, RequiredFieldIsEmptyException;
    SaleDTO addDessertUnits(String dessertCode, long saleId, int units) throws RemoteException, RequiredFieldIsEmptyException,
            DessertNotFoundException, SaleNotFoundException, InvalidSaleOperationException, MaxUnitsExceededException;
    void deleteDessertUnits(String dessertCode, long saleId, int units) throws RemoteException,
            SaleNotFoundException, InvalidSaleOperationException, DessertNotFoundException, InsufficientUnitsException;

    // Los siguientes métodos aún no están implementados en LogicLayer.
    // Para evitar errores de compilación mientras se desarrollan, se comentan temporalmente.
    /*
    void finalizeSale(long id, ude.edu.uy.taller2.collection.SaleDecision saleDecision) throws RemoteException;
    java.util.List<SaleDTO> getSalesByStatus(ude.edu.uy.taller2.collection.SaleFilter saleFilter) throws RemoteException;
    java.util.List<ude.edu.uy.taller2.dto.SaleItemDTO> getDessertsBySaleId(long id) throws RemoteException;
    ude.edu.uy.taller2.dto.SalesSummaryDTO getSalesByDessert(String code, LocalDate date) throws RemoteException;
    void saveData() throws RemoteException;
    void loadData() throws RemoteException;
    */
}
