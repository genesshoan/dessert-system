package ude.edu.uy.taller2.persistence;

import ude.edu.uy.taller2.collection.Desserts;
import ude.edu.uy.taller2.collection.Sales;
import ude.edu.uy.taller2.exception.AppDataPersistenceException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Persistence {
    private static final String FILE_NAME = "data/data.dat";

    public static void save(AppData appData) throws AppDataPersistenceException {
        File file = new File(FILE_NAME);

        // Crea el directorio "data/" si no llega a existir
        file.getParentFile().mkdirs();

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);) {

            objectOutputStream.writeObject(appData);
        } catch (IOException e) {
            throw new AppDataPersistenceException("Error while saving application data");
        }
    }

    public static AppData load() throws AppDataPersistenceException {
        if (!Files.exists(Path.of(FILE_NAME))) {
            return new AppData(new Desserts(), new Sales());
        }

        try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {

            return (AppData) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new AppDataPersistenceException("Error while loading application data");
        }
    }
}
