package ude.edu.uy.taller2.logic;

import ude.edu.uy.taller2.collection.SaleDecision;
import ude.edu.uy.taller2.collection.SaleFilter;
import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.dto.SaleDTO;
import ude.edu.uy.taller2.dto.SaleItemDTO;
import ude.edu.uy.taller2.dto.SalesSummaryDTO;
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
            SaleNotFoundException, InvalidSaleOperationException, DessertNotFoundException, InsufficientUnitsException

    void finalizeSale(long id, SaleDecision saleDecision) throws RemoteException;
    List<SaleDTO> getSalesByStatus(SaleFilter saleFilter) throws RemoteException;
    List<SaleItemDTO> getDessertsBySaleId(long id) throws RemoteException;
    SalesSummaryDTO getSalesByDessert(String code, LocalDate date) throws RemoteException;
    void saveData() throws RemoteException;
    void loadData() throws RemoteException;
}
