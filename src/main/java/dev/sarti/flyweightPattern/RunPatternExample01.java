package dev.sarti.flyweightPattern;

import dev.sarti.flyweightPattern.exampleProducts.Product;
import dev.sarti.flyweightPattern.exampleProducts.ProductFactory;
import dev.sarti.flyweightPattern.exampleProducts.ProductFlyweight;
import dev.sarti.flyweightPattern.exampleProducts.ProductRepository;

public class RunPatternExample01 {
    public static void main(String[] args) {
        ProductFactory factory = new ProductFactory();
        ProductFactory flyweightFactory = new ProductFactory();
        ProductRepository repository = new ProductRepository();

        int count = 1;
        // Simula 1000 productos compartiendo solo 3 combinaciones de categoría/marca
        for (int i = 0; i < 50000; i++) {
            String category;
            switch (i % 3) {
                case 0:
                    category = "Electronics";
                    break;
                case 1:
                    category = "Books";
                    break;
                default:
                    category = "Clothing";
                    break;
            }
            String brand;
            switch (i % 3) {
                case 0:
                    brand = "Sony";
                    break;
                case 1:
                    brand = "Penguin";
                    break;
                default:
                    brand = "Nike";
                    break;
            }

            ProductFlyweight shared = flyweightFactory.getFlyweight(category, brand);
            Product product = new Product(count++, "Product-" + i, (10 + i % 50), shared);
            repository.save(product);
        }

        // Muestra los primeros 5 productos
        repository.findAll().stream().limit(5).forEach(Product::printInfo);

        // Actualizar precio y nombre
        System.out.println("\n=== Actualizar producto 1 ===");
        Product p1 = repository.findById(1);
        p1.setName("TV 8K Ultra HD");
        p1.setPrice(1599.99);

        repository.findAll().stream().limit(5).forEach(Product::printInfo);

        repository.updateSafeMethod(p1);

        System.out.println("\n=== Actualizar producto 1 con método seguro ===");
        ProductFlyweight newFlyweight = factory.getFlyweight("Electronics", "LG");
        p1.setFlyweight(newFlyweight);

        // Muestra los primeros 5 productos
        repository.findAll().stream().limit(5).forEach(Product::printInfo);

        // Estadísticas
        System.out.println("\nTotal products: " + repository.findAll().size());
        System.out.println("Unique Flyweight instances: " + flyweightFactory.getFlyweightCount());
    }
}
