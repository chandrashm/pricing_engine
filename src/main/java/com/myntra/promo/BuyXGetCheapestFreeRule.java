package com.myntra.promo;

import java.math.BigDecimal;

public class BuyXGetCheapestFreeRule implements PromotionRule {
    private final String targetCategory;
    private final int minimumQuantity;

    public BuyXGetCheapestFreeRule(String targetCategory, int minimumQuantity) {
        if (targetCategory == null || targetCategory.isBlank()) {
            throw new IllegalArgumentException("Target category is required.");
        }
        if (minimumQuantity < 2) {
            throw new IllegalArgumentException("Minimum quantity must be at least 2.");
        }
        this.targetCategory = targetCategory;
        this.minimumQuantity = minimumQuantity;
    }

    @Override
    public void apply(Cart cart) {
        int totalCategoryQty = 0;
        BigDecimal cheapestUnitPrice = null;

        for (CartItem item : cart.getItems()) {
            if (targetCategory.equalsIgnoreCase(item.getCategory())) {
                totalCategoryQty += item.getQuantity();
                if (cheapestUnitPrice == null || item.getUnitPrice().compareTo(cheapestUnitPrice) < 0) {
                    cheapestUnitPrice = item.getUnitPrice();
                }
            }
        }

        if (totalCategoryQty >= minimumQuantity && cheapestUnitPrice != null) {
            cart.applyDiscount(
                "Buy " + minimumQuantity + " " + targetCategory + " Get Cheapest Free",
                cheapestUnitPrice
            );
        }
    }
}
