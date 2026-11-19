package com.myntra.promo;

import java.math.BigDecimal;

public class CartItem {
    private final String productId;
    private final String name;
    private final String brand;
    private final String category;
    private final BigDecimal unitPrice;
    private final int quantity;

    public CartItem(String productId, String name, String brand, String category, BigDecimal unitPrice, int quantity) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID is required.");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getCategory() { return category; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
}
