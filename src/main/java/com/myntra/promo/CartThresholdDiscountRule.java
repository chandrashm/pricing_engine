package com.myntra.promo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CartThresholdDiscountRule implements PromotionRule {
    private final BigDecimal minimumCartTotal;
    private final BigDecimal extraPercentageOff;

    public CartThresholdDiscountRule(BigDecimal minimumCartTotal, BigDecimal extraPercentageOff) {
        this.minimumCartTotal = minimumCartTotal;
        this.extraPercentageOff = extraPercentageOff;
    }

    @Override
    public void apply(Cart cart) {
        if (cart.getCurrentTotal().compareTo(minimumCartTotal) >= 0) {
            BigDecimal discount = cart.getCurrentTotal()
                    .multiply(extraPercentageOff)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            cart.applyDiscount("Cart Value Above Rs. " + minimumCartTotal + " (" + extraPercentageOff + "% Off)", discount);
        }
    }
}
