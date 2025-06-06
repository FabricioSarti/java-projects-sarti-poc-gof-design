package dev.sarti.FactoryPattern.factory;

import java.util.Calendar;
import java.util.Date;

import dev.sarti.FactoryPattern.dto.ProductDTO;
import dev.sarti.FactoryPattern.model.Product;

public class ProductFactory {
    public Product createProduct(ProductDTO dto, Long id) {
        validate(dto);
        boolean taxable = false;
        Date expirationDate = null;

        switch (dto.type) {
            case ELECTRONIC:
                taxable = true;
                break;
            case FOOD:
                expirationDate = getExpirationDate(7); // 7 días desde hoy
                break;
            case BOOK:
                taxable = false;
                break;
        }

        return new Product(id, dto.name, dto.price, dto.type, taxable, expirationDate);
    }

    private void validate(ProductDTO dto) {
        if (dto.name == null || dto.name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.price <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (dto.type == null) {
            throw new IllegalArgumentException("El tipo de producto es obligatorio.");
        }
    }

    private Date getExpirationDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}
