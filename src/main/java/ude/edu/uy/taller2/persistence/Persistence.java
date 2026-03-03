package ude.edu.uy.taller2.persistence;

import ude.edu.uy.taller2.collection.Desserts;
import ude.edu.uy.taller2.collection.Sales;
import ude.edu.uy.taller2.exception.AppDataPersistenceException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utilidad para guardar y cargar el estado de la aplicación en disco.
 * <p>
 * Implementa persistencia binaria simple usando serialización Java hacia
 * el archivo "data/data.dat". Provee métodos estáticos {@link #save(AppData)}
 * y {@link #load()}.
 */
public class Persistence {
    private static final String FILE_NAME = "data/data.dat";

    /**
     * Serializa y guarda el objeto {@link AppData} en disco.
     *
     * @param appData estado de la aplicación a persistir
     * @throws AppDataPersistenceException si ocurre un error durante la escritura
     */
    public static void save(AppData appData) throws AppDataPersistenceException {
        File file = new File(FILE_NAME);

        // Crea el directorio "data/" si no llega a existir
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(appData);
        } catch (IOException e) {
            throw new AppDataPersistenceException("Error while saving application data");
        }
    }

    /**
     * Carga el estado persistido de la aplicación si existe, o devuelve un
     * estado vacío si no existe el archivo de datos.
     *
     * @return instancia de {@link AppData} con el estado cargado o vacío
     * @throws AppDataPersistenceException si ocurre un error durante la lectura
     */
    public static AppData load() throws AppDataPersistenceException {
        if (!Files.exists(Path.of(FILE_NAME))) {
            return new AppData(new Desserts(), new Sales());
        }

        try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            return (AppData) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new AppDataPersistenceException("Error while loading application data");
        }
    }
}
