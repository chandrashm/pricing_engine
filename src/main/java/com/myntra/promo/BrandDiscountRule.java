package com.myntra.promo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BrandDiscountRule implements PromotionRule {
    private final String targetBrand;
    private final BigDecimal discountPercentage;

    public BrandDiscountRule(String targetBrand, BigDecimal discountPercentage) {
        if (targetBrand == null || targetBrand.isBlank()) {
            throw new IllegalArgumentException("Target brand is required.");
        }
        if (discountPercentage == null || discountPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount percentage cannot be negative.");
        }
        this.targetBrand = targetBrand;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public void apply(Cart cart) {
        BigDecimal brandItemsTotal = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            if (targetBrand.equalsIgnoreCase(item.getBrand())) {
                brandItemsTotal = brandItemsTotal.add(item.getTotalPrice());
            }
        }
        if (brandItemsTotal.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = brandItemsTotal
                    .multiply(discountPercentage)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            cart.applyDiscount(targetBrand + " " + discountPercentage + "% Off", discount);
        }
    }
}
