package dev.sarti.flyweightPattern.exampleProducts;

import java.util.HashMap;
import java.util.Map;

public class ProductFactory {

    private final Map<String, ProductFlyweight> flyweights = new HashMap<>();

    public ProductFlyweight getFlyweight(String category, String brand) {
        String key = category + "-" + brand;
        flyweights.putIfAbsent(key, new ProductFlyweight(category, brand));
        return flyweights.get(key);
    }

    public int getFlyweightCount() {
        return flyweights.size();
    }
}
