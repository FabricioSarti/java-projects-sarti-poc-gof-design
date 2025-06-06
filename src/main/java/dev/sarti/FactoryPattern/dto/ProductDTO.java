package dev.sarti.FactoryPattern.dto;

import dev.sarti.FactoryPattern.factory.ProductType;

public class ProductDTO {
    public String name;
    public double price;
    public ProductType type;

    public ProductDTO(String name, double price, ProductType type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }
}
