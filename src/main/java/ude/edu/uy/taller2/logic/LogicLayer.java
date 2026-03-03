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

/**
 * Capa lógica de la aplicación (Singleton remoto).
 * <p>
 * Provee la API que expone las operaciones de negocio: gestión de postres y
 * ventas. Implementa control de concurrencia mediante {@link Monitor} y
 * delega en las colecciones en memoria ( {@link Desserts} y {@link Sales} ) para
 * las operaciones persistidas en memoria. Esta clase hereda de
 * {@link UnicastRemoteObject} para poder ser publicada por RMI.
 */
public class LogicLayer extends UnicastRemoteObject implements ILogicLayer {
    private static volatile LogicLayer instance = null;

    private final Monitor monitor;
    private Sales sales;
    private Desserts desserts;

    /**
     * Constructor privado; inicializa el monitor y carga los datos persistidos.
     *
     * @throws RemoteException            si hay un error en la inicialización RMI
     * @throws AppDataPersistenceException si ocurre un error al cargar los datos
     */
    private LogicLayer() throws RemoteException, AppDataPersistenceException {
        this.monitor = new Monitor();
        loadData();
    }

    /**
     * Devuelve la instancia singleton de la capa lógica.
     *
     * @return instancia única de {@link LogicLayer}
     * @throws RemoteException            si ocurre un error RMI durante la creación
     * @throws AppDataPersistenceException si ocurre un error al cargar datos iniciales
     */
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

    /**
     * Crea un nuevo postre a partir de un DTO.
     *
     * @param dessertDTO DTO que describe el postre a crear
     * @throws RemoteException               en fallos RMI
     * @throws RequiredFieldIsEmptyException si falta un campo requerido
     * @throws DessertAlreadyExistsException si ya existe un postre con el código
     * @throws InvalidAmountException        si el precio es inválido
     */
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

            if (dessertDTO instanceof LightDessertDTO ld) {
                desserts.insert(new LightDessert(
                        ld.getCode(),
                        ld.getName(),
                        ld.getPrice(),
                        ld.getSweetener(),
                        ld.getDescription()));
            } else {
                desserts.insert(new Dessert(
                        dessertDTO.getCode(),
                        dessertDTO.getName(),
                        dessertDTO.getPrice()));
            }
        } finally {
            monitor.releaseWrite();
        }
    }

    /**
     * Recupera todos los postres disponibles como DTOs.
     *
     * @return lista de {@link ude.edu.uy.taller2.dto.DessertDTO}
     * @throws RemoteException en fallos RMI
     */
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

    /**
     * Obtiene un postre por su código y lo devuelve como DTO.
     *
     * @param code código del postre
     * @return DTO con los datos del postre
     * @throws RemoteException               en fallos RMI
     * @throws RequiredFieldIsEmptyException si el código está vacío
     * @throws DessertNotFoundException      si no se encuentra el postre
     */
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

    /**
     * Crea una nueva venta con dirección y fecha.
     *
     * @param address dirección de entrega
     * @param date    fecha de la venta
     * @throws RemoteException               en fallos RMI
     * @throws RequiredFieldIsEmptyException si la dirección está vacía
     * @throws InvalidDateException          si la fecha es anterior a la última venta
     */
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

    /**
     * Agrega unidades de un postre a una venta.
     *
     * @param dessertCode código del postre
     * @param saleId      id de la venta
     * @param units       unidades a agregar
     * @return DTO con el estado actualizado de la venta
     * @throws RemoteException               en fallos RMI
     * @throws RequiredFieldIsEmptyException si algún campo requerido está vacío
     * @throws DessertNotFoundException      si no existe el postre
     * @throws SaleNotFoundException         si no existe la venta
     * @throws InvalidSaleOperationException si la operación no es válida
     * @throws MaxUnitsExceededException     si supera el límite máximo por venta
     */
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

    /**
     * Elimina unidades de un postre en una venta.
     *
     * @param dessertCode código del postre
     * @param saleId      id de la venta
     * @param units       unidades a eliminar
     * @throws RemoteException               en fallos RMI
     * @throws SaleNotFoundException         si la venta no existe
     * @throws InvalidSaleOperationException si la operación no es válida
     * @throws DessertNotFoundException      si el postre no existe
     * @throws InsufficientUnitsException    si no hay unidades suficientes
     */
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

    /**
     * Finaliza o cancela una venta según la decisión indicada.
     *
     * @param id           id de la venta
     * @param saleDecision decisión (COMPLETE/CANCEL)
     * @throws RemoteException           en fallos RMI
     * @throws SaleNotFoundException     si la venta no existe
     * @throws InvalidSaleOperationException si la operación no es válida
     */
    public void finalizeSale(long id, SaleDecision saleDecision) throws RemoteException, SaleNotFoundException,
            InvalidSaleOperationException {
        monitor.requireWrite();

        try {
            sales.finalizeSale(id, saleDecision);
        } finally {
            monitor.releaseWrite();
        }
    }

    /**
     * Obtiene las ventas filtradas por estado y las transforma a DTOs.
     *
     * @param saleFilter filtro a aplicar
     * @return lista de {@link ude.edu.uy.taller2.dto.SaleDTO}
     * @throws RemoteException en fallos RMI
     */
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

    /**
     * Lista los items (postres) de una venta como {@link ude.edu.uy.taller2.dto.SaleItemDTO}.
     *
     * @param id id de la venta
     * @return lista de items de venta
     * @throws RemoteException       en fallos RMI
     * @throws SaleNotFoundException si la venta no existe
     */
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

    /**
     * Calcula el resumen de ventas por postre para una fecha dada delegando en la colección {@link Sales}.
     *
     * @param code código del postre
     * @param date fecha a consultar
     * @return {@link ude.edu.uy.taller2.dto.SalesSummaryDTO}
     * @throws RemoteException               en fallos RMI
     * @throws RequiredFieldIsEmptyException si el código está vacío
     * @throws DessertNotFoundException      si el postre no existe
     */
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

    /**
     * Persiste el estado actual de la aplicación.
     *
     * @throws RemoteException en fallos RMI durante la operación de guardado
     * @throws AppDataPersistenceException en errores de persistencia
     */
    public void saveData() throws RemoteException, AppDataPersistenceException {
        monitor.requireWrite();

        try {
            Persistence.save(new AppData(desserts, sales));
        } finally {
            monitor.releaseWrite();
        }
    }

    /**
     * Carga el estado persistido de la aplicación desde disco.
     *
     * @throws RemoteException en fallos RMI durante la operación de carga
     * @throws AppDataPersistenceException en errores de persistencia
     */
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

    /**
     * Utilidad rápida para comprobar si una cadena es nula o está vacía/blanco.
     *
     * @param s cadena a comprobar
     * @return true si la cadena es null o está en blanco
     */
    public static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }
}
