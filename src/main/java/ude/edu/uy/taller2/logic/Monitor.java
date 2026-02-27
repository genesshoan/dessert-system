package ude.edu.uy.taller2.logic;

/**
 * Monitor para control de concurrencia con múltiples lectores y un único escritor.
 * Permite acceso concurrente a lectores, pero acceso exclusivo a escritores.
 */
public class Monitor {
    private int numberOfReaders;
    private boolean isWriting;

    public Monitor() {
        numberOfReaders = 0;
        isWriting = false;
    }

    /**
     * Solicita acceso de lectura. Bloquea si hay un escritor activo.
     * Múltiples lectores pueden leer simultáneamente.
     */
    public synchronized void requireRead() {
        while (isWriting) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        numberOfReaders++;
    }

    /**
     * Libera el acceso de lectura. Si no quedan lectores activos,
     * notifica a los hilos en espera.
     */
    public synchronized void releaseRead() {
        numberOfReaders--;
        if (numberOfReaders == 0) {
            this.notifyAll();
        }
    }

    /**
     * Solicita acceso de escritura. Bloquea si hay otro escritor activo
     * o si hay lectores activos. Solo un escritor puede escribir a la vez.
     */
    public synchronized void requireWrite() {
        while (isWriting || numberOfReaders > 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        isWriting = true;
    }

    /**
     * Libera el acceso de escritura y notifica a todos los hilos en espera.
     */
    public synchronized void releaseWrite() {
        isWriting = false;
        this.notifyAll();
    }
}