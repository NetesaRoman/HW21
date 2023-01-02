package view;

import models.CSVWorker;

import java.io.IOException;

/*
 *
 * @author Roman Netesa
 *
 */
public class Main {
    public static void main(String[] args) {
        CSVWorker worker = new CSVWorker();
        final String ORDER1 = "src/main/resources/order_1.csv";
        final String ORDER2 = "src/main/resources/order_2.csv";

        try {
            worker.readFile(ORDER1);
            worker.readFile(ORDER2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        worker.createFiles("src/main/resources/");

    }
}
