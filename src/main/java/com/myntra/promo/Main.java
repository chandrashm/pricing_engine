package com.myntra.promo;

import java.math.BigDecimal;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        CartItem nikeShoes = new CartItem("P1", "Running Shoes", "Nike", "Footwear", new BigDecimal("4000.00"), 1);
        CartItem pumaSneakers = new CartItem("P2", "Casual Sneakers", "Puma", "Footwear", new BigDecimal("2500.00"), 1);
        CartItem shirt = new CartItem("P3", "Casual Shirt", "Levi's", "Topwear", new BigDecimal("2000.00"), 1);

        Cart cart = new Cart(Arrays.asList(nikeShoes, pumaSneakers, shirt));

        PromotionEngine engine = new PromotionEngine()
                .addRule(new BuyXGetCheapestFreeRule("Footwear", 2))
                .addRule(new BrandDiscountRule("Nike", new BigDecimal("10")))
                .addRule(new ComboDiscountRule("Footwear", "Topwear", new BigDecimal("500")))
                .addRule(new CartThresholdDiscountRule(new BigDecimal("5000"), new BigDecimal("5")));

        System.out.println("Original Cart Total: Rs. " + cart.calculateOriginalTotal());
        engine.checkout(cart);

        System.out.println("\nApplied Discounts:");
        for (String summary : cart.getAppliedDiscounts()) {
            System.out.println(" - " + summary);
        }
        System.out.println("\nFinal Payable Amount: Rs. " + cart.getCurrentTotal());
    }
}
