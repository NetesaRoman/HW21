package models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 *
 * @author Roman Netesa
 *
 */
public class CSVWorkerTest {
    private static CSVWorker worker;
    static final String PATH = "src/test/resources/order_1.csv";
    private static List<Product> readTestOrders;
    private static List<Product> writeTestOrders;

    @BeforeAll
    public static void init() {
        worker = new CSVWorker();
        readTestOrders = new ArrayList() {
            {
                add(new Product("АТБ", "Гречка", 30.25, 120));
                add(new Product("АТБ", "Сахар", 21.25, 170));
                add(new Product("АТБ", "Мука", 26.5, 80));
                add(new Product("Сильпо", "Гречка", 31.25, 24));
                add(new Product("Сильпо", "Сахар", 22.20, 107));
                add(new Product("Сильпо", "Мука", 24.2, 99));
                add(new Product("АТБ", "Гречка", 31.25, 120));
                add(new Product("АТБ", "Сахар", 20.25, 170));
                add(new Product("АТБ", "Мука", 23.5, 80));
                add(new Product("АТБ", "Молоко", 21.4, 110));
                add(new Product("Сильпо", "Гречка", 31.85, 24));
                add(new Product("Сильпо", "Сахар", 21.40, 107));
                add(new Product("Сильпо", "Мука", 22.2, 99));

            }
        };

        writeTestOrders = new ArrayList(){{
            add(new Product("АТБ", "Гречка", 30.75, 120));
            add(new Product("АТБ", "Сахар", 20.75, 170));
            add(new Product("АТБ", "Мука", 25.0, 80));
            add(new Product("АТБ", "Молоко", 21.4, 110));
        }};
    }

    @Test
    public void testRead() {
        try {
            worker.readFile(PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(readTestOrders.toString(),worker.getOrders().toString());

    }

    @Test
    public void testWrite(){
        CSVWorker checkWorker = new CSVWorker();

        try {
            worker.readFile(PATH);
            worker.createFiles("src/test/resources/");
            checkWorker.readFile("src/test/resources/АТБ_res.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(writeTestOrders.toString(),checkWorker.getOrders().toString());
    }
}
