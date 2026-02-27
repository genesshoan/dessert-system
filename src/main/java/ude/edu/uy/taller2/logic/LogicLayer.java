package ude.edu.uy.taller2.logic;

import ude.edu.uy.taller2.collection.Desserts;
import ude.edu.uy.taller2.collection.Sales;
import ude.edu.uy.taller2.domain.Dessert;
import ude.edu.uy.taller2.domain.LightDessert;
import ude.edu.uy.taller2.domain.Sale;
import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.dto.LightDessertDTO;
import ude.edu.uy.taller2.dto.SaleDTO;
import ude.edu.uy.taller2.exception.*;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LogicLayer extends UnicastRemoteObject implements ILogicLayer {
    private static LogicLayer instance = null;

    private final Monitor monitor;
    private final Sales sales;
    private final Desserts desserts;

    private LogicLayer() throws RemoteException {
        sales = new Sales();
        desserts = new Desserts();
        this.monitor = new Monitor();
    }

    public static LogicLayer getInstance() throws RemoteException {
        if (instance == null) {
            synchronized (LogicLayer.class) {
                if (instance == null) {
                    instance = new LogicLayer();
                }
            }
        }

        return instance;
    }

    public void createDessert(DessertDTO dessertDTO) throws RemoteException, RequiredFieldIsEmptyException,
            DessertAlreadyExistsException, InvalidAmountException {
        monitor.requireWrite();

        if (isNullOrBlank(dessertDTO.getCode())) {
            throw new RequiredFieldIsEmptyException("Dessert code cannot be empty");
        }

        if (desserts.member(dessertDTO.getCode())) {
            throw new DessertAlreadyExistsException("Dessert with code " + dessertDTO.getCode() + " already exists");
        }

        if (isNullOrBlank(dessertDTO.getName())) {
            throw new RequiredFieldIsEmptyException("Dessert name cannot be empty");
        }

        if (dessertDTO.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Dessert price must be positive");
        }

        desserts.insert(
                new Dessert(
                        dessertDTO.getCode(),
                        dessertDTO.getName(),
                        dessertDTO.getPrice()));

        monitor.releaseWrite();
    }

    public List<DessertDTO> getAllDesserts() throws RemoteException{
        monitor.requireRead();

        List<DessertDTO> result = new ArrayList<>();

        for (Dessert dessert : desserts.getDesserts()) {
            DessertDTO dessertDTO;
            if (dessert instanceof LightDessert) {
                dessertDTO = new LightDessertDTO(
                        dessert.getCode(),
                        dessert.getName(),
                        dessert.getPrice(),
                        ((LightDessert) dessert).getDescription(),
                        ((LightDessert) dessert).getSweetener());

            } else {
                dessertDTO = new DessertDTO(
                        dessert.getCode(),
                        dessert.getName(),
                        dessert.getPrice());
            }

            result.add(dessertDTO);
        }

        monitor.releaseRead();

        return result;
    }

    public DessertDTO getDessertByCode(String code) throws RemoteException, RequiredFieldIsEmptyException,
            DessertNotFoundException {
        monitor.requireRead();

        if (isNullOrBlank(code)) {
            throw new RequiredFieldIsEmptyException("The dessert code provided is invalid");
        }

        Dessert dessert = desserts.find(code);

        if (dessert == null) {
            throw new DessertNotFoundException("Dessert with provided code was not found");
        }

        monitor.releaseRead();

        return new DessertDTO(
                dessert.getCode(),
                dessert.getName(),
                dessert.getPrice()
        );
    }

    public void createSale(String address, LocalDate date) throws RemoteException, RequiredFieldIsEmptyException {
        monitor.requireWrite();

        if (isNullOrBlank(address)) {
            throw new RequiredFieldIsEmptyException("The address cannot be empty");
        }

        LocalDate lastDate = sales.getLastSaleDate();

        if (lastDate != null && (date.isAfter(lastDate) || date.isEqual(lastDate))) {
            throw new InvalidDateException("The sale date must be equal or after the last sale date");
        }

        Sale sale = new Sale(0, date, address);

        sales.insert(sale);

        monitor.releaseWrite();
    }

    public SaleDTO addDessertUnits(String dessertCode, long saleId, int units) throws RemoteException, RequiredFieldIsEmptyException,
            DessertNotFoundException, SaleNotFoundException, InvalidSaleOperationException, MaxUnitsExceededException {
        monitor.requireWrite();

        if (isNullOrBlank(dessertCode)) {
            throw new RequiredFieldIsEmptyException("Dessert code cannot be empty");
        }

        if (!desserts.member(dessertCode)) {
            throw new DessertNotFoundException("No dessert with provided code was found");
        }

        if (!sales.existsById(saleId)) {
            throw new SaleNotFoundException("No sale with provided id was found");
        }

        Sale sale = sales.find(saleId);
        sale.addDessertUnits(
                desserts.find(dessertCode),
                units);

        monitor.releaseWrite();

        return new SaleDTO(
                sale.getId(),
                sale.getDate(),
                sale.getAddress(),
                sale.getStatus());
    }

    public void deleteDessertUnits(String dessertCode, long saleId, int units) throws RemoteException,
            SaleNotFoundException, InvalidSaleOperationException, DessertNotFoundException, InsufficientUnitsException {
        monitor.requireWrite();

        // Corregimos la condición: si NO existe el postre, lanzamos la excepción
        if (!desserts.member(dessertCode)){
            throw new DessertNotFoundException("There is no dessert with the provided identifier");
        }

        if (!sales.existsById(saleId)){
            throw new SaleNotFoundException("There is no sale process with the provided identifier");
        }

        sales.deleteDessertUnits(saleId, dessertCode, units);

        monitor.releaseWrite();
    }

    public static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }
}
