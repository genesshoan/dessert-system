package ude.edu.uy.taller2.logic;

import ude.edu.uy.taller2.server.collection.SaleDecision;
import ude.edu.uy.taller2.server.collection.SaleFilter;
import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.dto.SaleDTO;
import ude.edu.uy.taller2.dto.SaleItemDTO;
import ude.edu.uy.taller2.dto.SalesSummaryDTO;
import ude.edu.uy.taller2.exception.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz que define la API remota de la capa lógica de la aplicación.
 * Todas las operaciones lanzan RemoteException además de las excepciones de negocio
 * para que el cliente pueda manejarlas explícitamente.
 */
public interface ILogicLayer extends Remote {
    /**
     * Crea un nuevo postre en el catálogo.
     *
     * @param dessertDTO descripción del postre a crear
     * @throws RemoteException                   en fallos de comunicación remota
     * @throws RequiredFieldIsEmptyException     si algún campo requerido está vacío
     * @throws DessertAlreadyExistsException     si ya existe un postre con el mismo código
     * @throws InvalidAmountException            si el precio es inválido
     */
    void createDessert(DessertDTO dessertDTO) throws RemoteException, RequiredFieldIsEmptyException,
            DessertAlreadyExistsException, InvalidAmountException;

    /**
     * Devuelve todos los postres disponibles.
     *
     * @return lista de {@link DessertDTO}
     * @throws RemoteException en fallos de comunicación remota
     */
    List<DessertDTO> getAllDesserts() throws RemoteException;

    /**
     * Obtiene un postre por su código.
     *
     * @param code código del postre
     * @return {@link DessertDTO} correspondiente
     * @throws RemoteException               en fallos de comunicación remota
     * @throws RequiredFieldIsEmptyException si el código está vacío
     * @throws DessertNotFoundException      si no existe el postre
     */
    DessertDTO getDessertByCode(String code) throws RemoteException, RequiredFieldIsEmptyException,
            DessertNotFoundException;

    /**
     * Crea una nueva venta con dirección y fecha indicadas.
     *
     * @param address dirección de la venta
     * @param date    fecha de la venta
     * @throws RemoteException               en fallos de comunicación remota
     * @throws RequiredFieldIsEmptyException si la dirección está vacía
     * @throws InvalidDateException          si la fecha es inválida
     */
    void createSale(String address, LocalDate date) throws RemoteException, RequiredFieldIsEmptyException, InvalidDateException;

    /**
     * Agrega unidades de un postre a una venta existente.
     *
     * @param dessertCode código del postre
     * @param saleId      identificador de la venta
     * @param units       unidades a agregar
     * @return {@link SaleDTO} con el estado actualizado de la venta
     * @throws RemoteException               en fallos de comunicación remota
     * @throws RequiredFieldIsEmptyException si algún campo requerido está vacío
     * @throws DessertNotFoundException      si no existe el postre
     * @throws SaleNotFoundException         si no existe la venta
     * @throws InvalidSaleOperationException si la operación no es válida (venta finalizada, por ejemplo)
     * @throws MaxUnitsExceededException     si se intenta agregar más unidades que el límite permitido
     */
    SaleDTO addDessertUnits(String dessertCode, long saleId, int units) throws RemoteException, RequiredFieldIsEmptyException,
            DessertNotFoundException, SaleNotFoundException, InvalidSaleOperationException, MaxUnitsExceededException;

    /**
     * Elimina unidades de un postre de una venta.
     *
     * @param dessertCode código del postre
     * @param saleId      identificador de la venta
     * @param units       unidades a eliminar
     * @throws RemoteException               en fallos de comunicación remota
     * @throws SaleNotFoundException         si no existe la venta
     * @throws InvalidSaleOperationException si la operación no es válida
     * @throws DessertNotFoundException      si no existe el postre en la venta
     * @throws InsufficientUnitsException    si se intenta eliminar más unidades de las existentes
     */
    void deleteDessertUnits(String dessertCode, long saleId, int units) throws RemoteException,
            SaleNotFoundException, InvalidSaleOperationException, DessertNotFoundException, InsufficientUnitsException;

    /**
     * Finaliza o cancela una venta.
     *
     * @param id           identificador de la venta
     * @param saleDecision acción a realizar (COMPLETE/CANCEL)
     * @throws RemoteException           en fallos de comunicación remota
     * @throws SaleNotFoundException     si no existe la venta
     * @throws InvalidSaleOperationException si la operación no es válida
     */
    void finalizeSale(long id, SaleDecision saleDecision) throws RemoteException, SaleNotFoundException,
            InvalidSaleOperationException;

    /**
     * Obtiene las ventas filtradas por estado.
     *
     * @param saleFilter filtro de ventas
     * @return lista de {@link SaleDTO}
     * @throws RemoteException en fallos de comunicación remota
     */
    List<SaleDTO> getSalesByStatus(SaleFilter saleFilter) throws RemoteException;

    /**
     * Lista los items (postres) de una venta.
     *
     * @param id identificador de la venta
     * @return lista de {@link SaleItemDTO}
     * @throws RemoteException       en fallos de comunicación remota
     * @throws SaleNotFoundException si no existe la venta
     */
    List<SaleItemDTO> getDessertsBySaleId(long id) throws RemoteException, SaleNotFoundException;

    /**
     * Obtiene el resumen de ventas por postre para una fecha dada.
     *
     * @param code código del postre
     * @param date fecha para el resumen
     * @return {@link SalesSummaryDTO} con unidades y monto total
     * @throws RemoteException               en fallos de comunicación remota
     * @throws RequiredFieldIsEmptyException si el código está vacío
     * @throws DessertNotFoundException      si no existe el postre
     */
    SalesSummaryDTO getSalesByDessert(String code, LocalDate date) throws RemoteException, RequiredFieldIsEmptyException,
            DessertNotFoundException;

    /**
     * Persiste el estado actual de la aplicación.
     *
     * @throws RemoteException
     * @throws AppDataPersistenceException en errores de persistencia
     */
    void saveData() throws RemoteException, AppDataPersistenceException;

    /**
     * Carga el estado persistido de la aplicación.
     *
     * @throws RemoteException
     * @throws AppDataPersistenceException en errores de persistencia
     */
    void loadData() throws RemoteException, AppDataPersistenceException;
}
