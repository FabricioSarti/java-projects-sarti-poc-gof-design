package dev.sarti.FactoryPattern;

import dev.sarti.FactoryPattern.dto.ProductDTO;
import dev.sarti.FactoryPattern.factory.ProductType;
import dev.sarti.FactoryPattern.service.ProductService;

public class RunPatternFactoryExample01 {

    public static void main(String[] args) {
        ProductService service = new ProductService();

        System.out.println("=== CREANDO PRODUCTOS ===");
        try {
            service.createProduct(new ProductDTO("Laptop", 1200, ProductType.ELECTRONIC));
            service.createProduct(new ProductDTO("Manzana", 1.5, ProductType.FOOD));
            service.createProduct(new ProductDTO("Libro de Java", 45, ProductType.BOOK));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("=== LISTANDO PRODUCTOS ===");
        service.listAll().forEach(product -> System.out.println(product.toString()));

        System.out.println("=== ACTUALIZANDO PRODUCTO ===");
        try {
            service.updateProduct(1L, new ProductDTO("Laptop", 1100, ProductType.ELECTRONIC));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== LISTADO ACTUALIZADO ===");
        service.listAll().forEach(System.out::println);

        System.out.println("\n=== ELIMINANDO PRODUCTO ID 1 ===");
        service.deleteProduct(1L);

        System.out.println("\n=== LISTADO FINAL ===");
        service.listAll().forEach(System.out::println);
    }

}
