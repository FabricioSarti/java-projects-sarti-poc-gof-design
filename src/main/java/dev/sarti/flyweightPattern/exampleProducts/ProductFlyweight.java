package dev.sarti.flyweightPattern.exampleProducts;

public class ProductFlyweight {

    private final String category;
    private final String brand;

    public ProductFlyweight(String category, String brand) {
        this.category = category;
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

}
