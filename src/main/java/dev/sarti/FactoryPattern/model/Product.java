package dev.sarti.FactoryPattern.model;

import java.util.Date;

import dev.sarti.FactoryPattern.factory.ProductType;

public class Product {
    public Long id;
    public String name;
    public double price;
    public ProductType type;
    public boolean taxable;
    public Date expirationDate;

    public Product(Long id, String name, double price, ProductType type, boolean taxable, Date expirationDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.taxable = taxable;
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return String.format("[ID: %d] %s - $%.2f - Type: %s - Taxable: %b - Exp: %s",
                id, name, price, type, taxable,
                (expirationDate != null ? expirationDate.toString() : "N/A"));
    }
}
