package dev.sarti.FactoryPattern.service;

import java.util.List;
import java.util.Optional;

import dev.sarti.FactoryPattern.dto.ProductDTO;
import dev.sarti.FactoryPattern.factory.ProductFactory;
import dev.sarti.FactoryPattern.model.Product;
import dev.sarti.FactoryPattern.repository.ProductRepository;

public class ProductService {
    private final ProductFactory factory = new ProductFactory();
    private final ProductRepository repository = new ProductRepository();

    public Product createProduct(ProductDTO dto) {
        Product product = factory.createProduct(dto, null);
        return repository.create(product);
    }

    public Product updateProduct(Long id, ProductDTO dto) {
        Product product = factory.createProduct(dto, id);
        return repository.update(id, product);
    }

    public List<Product> listAll() {
        return repository.findAll();
    }

    public boolean deleteProduct(Long id) {
        return repository.delete(id);
    }

    public Optional<Product> getById(Long id) {
        return repository.findById(id);
    }
}
