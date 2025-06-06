package dev.sarti.flyweightPattern.exampleProducts;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private final List<Product> products = new ArrayList<>();

    public void save(Product product) {
        products.add(product);
    }

    public List<Product> findAll() {
        return products;
    }

    public Product findById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void deleteById(int id) {
        products.removeIf(p -> p.getId() == id);
    }

    public void updateSafeMethod(Product productToUpdate) {
        Product existing = findById(productToUpdate.getId());
        if (existing != null) {
            existing.setName(productToUpdate.getName());
            existing.setPrice(productToUpdate.getPrice());
            existing.setFlyweight(productToUpdate.getFlyweight());
        }
    }
}
