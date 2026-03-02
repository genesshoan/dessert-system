package ude.edu.uy.taller2.logic;

import ude.edu.uy.taller2.collection.Desserts;
import ude.edu.uy.taller2.collection.SaleDecision;
import ude.edu.uy.taller2.collection.SaleFilter;
import ude.edu.uy.taller2.collection.Sales;
import ude.edu.uy.taller2.domain.Dessert;
import ude.edu.uy.taller2.domain.LightDessert;
import ude.edu.uy.taller2.domain.Sale;
import ude.edu.uy.taller2.dto.*;
import ude.edu.uy.taller2.exception.*;
import ude.edu.uy.taller2.persistence.AppData;
import ude.edu.uy.taller2.persistence.Persistence;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LogicLayer extends UnicastRemoteObject implements ILogicLayer {
    private static LogicLayer instance = null;

    private final Monitor monitor;
    private Sales sales;
    private Desserts desserts;

    private LogicLayer() throws RemoteException, AppDataPersistenceException {
        this.monitor = new Monitor();
        loadData();
    }

    public static LogicLayer getInstance() throws RemoteException, AppDataPersistenceException {
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

        try {
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
        } finally {
            monitor.releaseWrite();
        }
    }

    public List<DessertDTO> getAllDesserts() throws RemoteException{
        monitor.requireRead();

        try {
            List<DessertDTO> result = new ArrayList<>();

            for (Dessert dessert : desserts.getDesserts()) {
                DessertDTO dessertDTO;
                if (dessert instanceof LightDessert) {
                    dessertDTO = new LightDessertDTO(
                            dessert.getCode(),
                            dessert.getName(),
                            dessert.getPrice(),
                            ((LightDessert) dessert).getSweetener(),
                            ((LightDessert) dessert).getDescription());

                } else {
                    dessertDTO = new DessertDTO(
                            dessert.getCode(),
                            dessert.getName(),
                            dessert.getPrice());
                }

                result.add(dessertDTO);
            }

            return result;
        } finally {
            monitor.releaseRead();
        }
    }

    public DessertDTO getDessertByCode(String code) throws RemoteException, RequiredFieldIsEmptyException,
            DessertNotFoundException {
        monitor.requireRead();

        try {
            if (isNullOrBlank(code)) {
                throw new RequiredFieldIsEmptyException("The dessert code provided is invalid");
            }

            Dessert dessert = desserts.find(code);

            if (dessert == null) {
                throw new DessertNotFoundException("Dessert with provided code was not found");
            }

            if (dessert instanceof LightDessert ld) {
                return new LightDessertDTO(
                        ld.getCode(),
                        ld.getName(),
                        ld.getPrice(),
                        ld.getSweetener(),
                        ld.getDescription());
            }

            return new DessertDTO(
                    dessert.getCode(),
                    dessert.getName(),
                    dessert.getPrice()
            );
        } finally {
            monitor.releaseRead();
        }
    }

    public void createSale(String address, LocalDate date) throws RemoteException, RequiredFieldIsEmptyException,
            InvalidDateException {
        monitor.requireWrite();

        try {
            if (isNullOrBlank(address)) {
                throw new RequiredFieldIsEmptyException("The address cannot be empty");
            }

            LocalDate lastDate = sales.getLastSaleDate();

            if (lastDate != null && date.isBefore(lastDate)) {
                throw new InvalidDateException("The sale date must be equal or after the last sale date");
            }

            Sale sale = new Sale(0, date, address);

            sales.insert(sale);
        } finally {
            monitor.releaseWrite();
        }
    }

    public SaleDTO addDessertUnits(String dessertCode, long saleId, int units) throws RemoteException, RequiredFieldIsEmptyException,
            DessertNotFoundException, SaleNotFoundException, InvalidSaleOperationException, MaxUnitsExceededException {
        monitor.requireWrite();

        try {
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

            return new SaleDTO(
                    sale.getId(),
                    sale.getDate(),
                    sale.getAddress(),
                    sale.getStatus());
        } finally {
            monitor.releaseWrite();
        }
    }

    public void deleteDessertUnits(String dessertCode, long saleId, int units) throws RemoteException,
            SaleNotFoundException, InvalidSaleOperationException, DessertNotFoundException, InsufficientUnitsException {
        monitor.requireWrite();

        try {
            if (!desserts.member(dessertCode)) {
                throw new DessertNotFoundException("There is no dessert with the provided identifier");
            }

            if (!sales.existsById(saleId)) {
                throw new SaleNotFoundException("There is no sale process with the provided identifier");
            }

            sales.deleteDessertUnits(saleId, dessertCode, units);
        } finally {
            monitor.releaseWrite();
        }
    }

    public void finalizeSale(long id, SaleDecision saleDecision) throws RemoteException, SaleNotFoundException,
            InvalidSaleOperationException {
        monitor.requireWrite();

        try {
            sales.finalizeSale(id, saleDecision);
        } finally {
            monitor.releaseWrite();
        }
    }

    public List<SaleDTO> getSalesByStatus(SaleFilter saleFilter) throws RemoteException {
        monitor.requireRead();

        try {
            return sales.getByStatus(saleFilter).stream()
                    .map((s) -> new SaleDTO(
                            s.getId(),
                            s.getDate(),
                            s.getAddress(),
                            s.getStatus()))
                    .toList();
        } finally {
            monitor.releaseRead();
        }
    }

    public List<SaleItemDTO> getDessertsBySaleId(long id) throws RemoteException, SaleNotFoundException {
        monitor.requireRead();

        try {
             if (!sales.existsById(id)) {
                 throw new SaleNotFoundException("No sale with id " + id + " was found");
             }

             return sales.getDessertsBySaleId(id).stream()
                     .map((si) -> {
                         Dessert dessert = si.getDessert();
                         DessertDTO dessertDTO = new DessertDTO(
                                 dessert.getCode(),
                                 dessert.getName(),
                                 dessert.getPrice());

                         return new SaleItemDTO(si.getQuantity(), dessertDTO);
                     }).toList();
        } finally {
            monitor.releaseRead();
        }
    }

    public SalesSummaryDTO getSalesByDessert(String code, LocalDate date) throws RemoteException, RequiredFieldIsEmptyException,
            DessertNotFoundException {
        monitor.requireRead();

        try {
            if (isNullOrBlank(code)) {
                throw new RequiredFieldIsEmptyException("Dessert code cannot be empty");
            }

            if (!desserts.member(code)) {
                throw new DessertNotFoundException("No dessert with code " + code + " was found");
            }

            return sales.getSalesByDessert(code, date);
        } finally {
            monitor.releaseRead();
        }
    }

    public void saveData() throws RemoteException, AppDataPersistenceException {
        monitor.requireWrite();

        try {
            Persistence.save(new AppData(desserts, sales));
        } finally {
            monitor.releaseWrite();
        }
    }

    public void loadData() throws RemoteException, AppDataPersistenceException {
        monitor.requireWrite();

        try {
            AppData appData = Persistence.load();
            desserts = appData.getDesserts();
            sales = appData.getSales();
        } finally {
            monitor.releaseWrite();
        }
    }

    public static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }
}
