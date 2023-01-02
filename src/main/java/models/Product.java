package models;

import lombok.Data;

/*
 *
 * @author Roman Netesa
 *
 */
@Data
public class Product {
    private String shopName;
    private String name;
    private double price;
    private long count;


    public Product(String shopName, String name, double price, long count) {
        this.shopName = shopName;
        this.name = name;
        this.price = price;
        this.count = count;
    }

}
