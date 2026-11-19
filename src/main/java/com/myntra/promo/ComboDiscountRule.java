package com.myntra.promo;

import java.math.BigDecimal;

public class ComboDiscountRule implements PromotionRule {
    private final String categoryA;
    private final String categoryB;
    private final BigDecimal flatDiscount;

    public ComboDiscountRule(String categoryA, String categoryB, BigDecimal flatDiscount) {
        this.categoryA = categoryA;
        this.categoryB = categoryB;
        this.flatDiscount = flatDiscount;
    }

    @Override
    public void apply(Cart cart) {
        boolean hasCategoryA = false;
        boolean hasCategoryB = false;

        for (CartItem item : cart.getItems()) {
            if (categoryA.equalsIgnoreCase(item.getCategory())) hasCategoryA = true;
            if (categoryB.equalsIgnoreCase(item.getCategory())) hasCategoryB = true;
        }
        if (hasCategoryA && hasCategoryB) {
            cart.applyDiscount("Combo Offer (" + categoryA + " + " + categoryB + ")", flatDiscount);
        }
    }
}
