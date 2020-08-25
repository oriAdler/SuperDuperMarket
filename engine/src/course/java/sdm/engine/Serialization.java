package course.java.sdm.engine;

import course.java.sdm.order.OrderStatic;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Serialization {
    public static final String FILE_NAME = "orders.dat";

    protected static Map<Integer, OrderStatic> readOrdersFromFile() throws IOException, ClassNotFoundException {
        // Read the map from the file
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(FILE_NAME))) {
            Map<Integer, OrderStatic> ordersFromFile =
            // we know that we read array list of Persons
                    (Map<Integer, OrderStatic>) in.readObject();
            System.out.println("ordersFromFile = " +
                    ordersFromFile);
            return ordersFromFile;
        }
    }

    protected static void writeOrdersToFile(Map<Integer, OrderStatic> orders) throws IOException {
        //Construct an ArrayList of persons:

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(FILE_NAME))) {
            out.writeObject(orders);
            out.flush();
        }
    }
}
