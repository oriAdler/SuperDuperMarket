package engine;

import sdm.order.OrderStatic;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;

public class Serialization {

    protected static Map<Integer, OrderStatic> readOrdersFromFile(Path path) throws IOException, ClassNotFoundException {
        // Read the map from the file
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(String.valueOf(path)))) {
            // We know that we read map of 'OrderStatic' objects
            return  (Map<Integer, OrderStatic>) in.readObject();
        }
    }

    protected static void writeOrdersToFile(Map<Integer, OrderStatic> orders, Path path) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(String.valueOf(path)))) {
            out.writeObject(orders);
            out.flush();
        }
    }
}
