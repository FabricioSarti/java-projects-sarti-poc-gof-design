package dev.sarti.flyweightPattern.exampleProducts;

public class Product {

    private final int id;
    private String name;
    private double price;
    private ProductFlyweight flyweight;

    public Product(int id, String name, double price, ProductFlyweight flyweight) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.flyweight = flyweight;
    }

    public void printInfo() {
        System.out.printf("ID: %d | Name: %s | Price: %.2f | Category: %s | Brand: %s%n",
                id, name, price,
                flyweight.getCategory(),
                flyweight.getBrand());
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setFlyweight(ProductFlyweight flyweight) {
        this.flyweight = flyweight;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public ProductFlyweight getFlyweight() {
        return flyweight;
    }
}
