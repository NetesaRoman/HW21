package models;

import api.UnknownLineFormatException;
import com.sun.istack.internal.NotNull;
import lombok.Cleanup;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/*
 *
 * @author Roman Netesa
 *
 */
public class CSVWorker {


    @Getter
    private List<Product> orders = new ArrayList<>();
    private List<List<Product>> sortedOrders = new ArrayList<>();


    public void readFile(@NotNull String file) throws IOException {
        @Cleanup FileReader fr = new FileReader(file);
        @Cleanup BufferedReader reader = new BufferedReader(fr);

        try {
            String line = reader.readLine();
            int counter = 0;

            while (line != null) {
                if (counter != 0) {
                    orders.add(parseToProduct(line));
                }
                counter++;
                line = reader.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void createFiles(String directory) {
        splitByShops();
        String fileName;
        File file;
        FileWriter fileWriter;
        Product avgProduct;


        for (List<Product> pl : sortedOrders) {
            fileName = directory + pl.get(0).getShopName() + "_res.csv";
            file = new File(fileName);
            List<String> names = getDistinctNames();
            try {
                if (file.createNewFile()) {
                    fileWriter = new FileWriter(file);
                    fileWriter.write("ИМЯ;НАИМЕНОВАНИЕ;ЦЕНА;ШТ;\n");
                    for (String name : names) {
                        avgProduct = findAvg(pl, name, pl.get(0).getShopName());
                        fileWriter.write(avgProduct.getShopName() + ";"
                                + avgProduct.getName() + ";"
                                + avgProduct.getPrice() + ";"
                                + avgProduct.getCount() + ";\n");
                    }
                    fileWriter.close();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void splitByShops() {
        int i = 0;
        List<String> shops = getDistinctShops();


        for (String shop : shops) {
            sortedOrders.add(new ArrayList<>());
            for (Product product : orders) {
                if (product.getShopName().equals(shop)) {
                    sortedOrders.get(i).add(product);
                }
            }
            i++;
        }
    }

    private Product findAvg(List<Product> pl, String name, String shopName) {
        Product result;
        final double[] avgPrice = {0};
        final int[] avgCount = {0};

        AtomicInteger i = new AtomicInteger(0);
        pl.stream()
                .filter(p -> Objects.equals(p.getName(), name))
                .forEach(p -> {
                    avgPrice[0] += p.getPrice();
                    avgCount[0] += p.getCount();
                    i.getAndIncrement();
                });
        if(i.get() != 0) {
            avgPrice[0] /= i.get();
            avgCount[0] /= i.get();
        }

        result = new Product(shopName, name, avgPrice[0], avgCount[0]);
        return result;
    }

    private List<String> getDistinctShops() {
        List<String> allShops = new ArrayList<>();
        List<String> shops = new ArrayList<>();

        for (Product product : orders) {
            allShops.add(product.getShopName());
        }
        allShops.stream().distinct().forEach(shops::add);

        return shops;
    }

    private List<String> getDistinctNames() {
        List<String> allNames = new ArrayList<>();
        List<String> names = new ArrayList<>();

        for (Product product : orders) {
            allNames.add(product.getName());
        }
        allNames.stream().distinct().forEach(names::add);

        return names;
    }

    private Product parseToProduct(@NotNull String line) {
        String[] splittedLine = line.split(";");
        try {
            handleExceptions(splittedLine, line);
        } catch (UnknownLineFormatException e) {
            e.printStackTrace();
        }

        String shopName = splittedLine[0];
        String name = splittedLine[1];
        double price = Double.parseDouble(splittedLine[2]);
        long count = Long.parseLong(splittedLine[3]);

        return new Product(shopName, name, price, count);
    }

    private void handleExceptions(@NotNull String[] splittedLine,
                                  @NotNull String line) throws UnknownLineFormatException {

        if (splittedLine.length != 4) {
            throw new UnknownLineFormatException(line);
        }
    }

}
