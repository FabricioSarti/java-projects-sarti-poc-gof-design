package dev.sarti.FactoryPattern.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import dev.sarti.FactoryPattern.model.Product;

public class ProductRepository {

    private final Map<Long, Product> db = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public Product save(Product product) {
        db.put(product.id, product);
        return product;
    }

    public Product create(Product product) {
        product.id = sequence.getAndIncrement();
        return save(product);
    }

    public List<Product> findAll() {
        return new ArrayList<>(db.values());
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    public boolean delete(Long id) {
        return db.remove(id) != null;
    }

    public Product update(Long id, Product updated) {
        if (!db.containsKey(id))
            throw new NoSuchElementException("No existe el producto con id: " + id);
        updated.id = id;
        return save(updated);
    }
}
