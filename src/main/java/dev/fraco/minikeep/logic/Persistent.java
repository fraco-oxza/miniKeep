package dev.fraco.minikeep.logic;

import java.io.IOException;

/**
 * The `Persistent` interface represents an object that can be loaded from and saved to a persistent storage.
 */
public interface Persistent {
    /**
     * Loads the object from a persistent storage.
     *
     * @throws IOException            If an I/O error occurs while reading the storage.
     * @throws ClassNotFoundException If the class of the object being loaded is not found.
     */
    void load() throws IOException, ClassNotFoundException;

    /**
     * Saves the object to a persistent storage.
     *
     * @throws IOException If an I/O error occurs while writing the storage.
     */
    void save() throws IOException;
}