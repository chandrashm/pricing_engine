package com.myntra.promo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PromotionEngineTest {

    @Test
    @DisplayName("Happy Path: Brand discount applies only to matching brand items")
    void testBrandDiscountRule_AppliesOnlyToTargetBrand() {
        CartItem nikeShoes = new CartItem("P1", "Shoes", "Nike", "Footwear", new BigDecimal("4000.00"), 1);
        CartItem pumaShirt = new CartItem("P2", "T-Shirt", "Puma", "Topwear", new BigDecimal("1000.00"), 1);
        Cart cart = new Cart(Arrays.asList(nikeShoes, pumaShirt));

        PromotionRule nikeRule = new BrandDiscountRule("Nike", new BigDecimal("10"));
        nikeRule.apply(cart);

        assertEquals(new BigDecimal("4600.00"), cart.getCurrentTotal());
        assertEquals(1, cart.getAppliedDiscounts().size());
    }

    @Test
    @DisplayName("Happy Path: Buy X Get Cheapest Free applies cheapest unit price in category")
    void testBuyXGetCheapestFreeRule_AppliesCheapestItem() {
        CartItem expensiveShoes = new CartItem("P1", "Pro Shoes", "Nike", "Footwear", new BigDecimal("4500.00"), 1);
        CartItem cheaperShoes = new CartItem("P2", "Daily Sneakers", "Puma", "Footwear", new BigDecimal("2200.00"), 1);
        Cart cart = new Cart(Arrays.asList(expensiveShoes, cheaperShoes));

        PromotionRule bogoRule = new BuyXGetCheapestFreeRule("Footwear", 2);
        bogoRule.apply(cart);

        assertEquals(new BigDecimal("4500.00"), cart.getCurrentTotal());
    }

    @Test
    @DisplayName("Happy Path: Full chain passes updated total from one rule to the next")
    void testPromotionEngine_ChainedRulesOrderMatters() {
        CartItem shoes = new CartItem("P1", "Shoes", "Nike", "Footwear", new BigDecimal("4000.00"), 1);
        CartItem shirt = new CartItem("P2", "Shirt", "Levi's", "Topwear", new BigDecimal("2000.00"), 1);
        CartItem watch = new CartItem("P3", "Watch", "Titan", "Accessories", new BigDecimal("1500.00"), 1);
        Cart cart = new Cart(Arrays.asList(shoes, shirt, watch));

        PromotionEngine engine = new PromotionEngine()
                .addRule(new BrandDiscountRule("Nike", new BigDecimal("10")))
                .addRule(new ComboDiscountRule("Footwear", "Topwear", new BigDecimal("500")))
                .addRule(new CartThresholdDiscountRule(new BigDecimal("5000"), new BigDecimal("10")));

        engine.checkout(cart);

        assertEquals(new BigDecimal("5940.00"), cart.getCurrentTotal());
        assertEquals(3, cart.getAppliedDiscounts().size());
    }

    @Test
    @DisplayName("Edge Case: Earlier discount drops total below threshold for later rule")
    void testChainedRules_EarlierDiscountDisqualifiesLaterThresholdRule() {
        CartItem shoes = new CartItem("P1", "Shoes", "Nike", "Footwear", new BigDecimal("5200.00"), 1);
        Cart cart = new Cart(Collections.singletonList(shoes));

        PromotionEngine engine = new PromotionEngine()
                .addRule(new BrandDiscountRule("Nike", new BigDecimal("10")))
                .addRule(new CartThresholdDiscountRule(new BigDecimal("5000"), new BigDecimal("5")));

        engine.checkout(cart);

        assertEquals(new BigDecimal("4680.00"), cart.getCurrentTotal());
        assertEquals(1, cart.getAppliedDiscounts().size());
    }

    @Test
    @DisplayName("Edge Case: Total discount exceeding cart value floors cleanly at 0.00")
    void testCartDiscount_NeverDropsBelowZero() {
        CartItem socks = new CartItem("P1", "Socks", "Nike", "Accessories", new BigDecimal("300.00"), 1);
        CartItem cap = new CartItem("P2", "Cap", "Puma", "Headwear", new BigDecimal("200.00"), 1);
        Cart cart = new Cart(Arrays.asList(socks, cap));

        PromotionRule hugeCombo = new ComboDiscountRule("Accessories", "Headwear", new BigDecimal("1000.00"));
        hugeCombo.apply(cart);

        assertEquals(new BigDecimal("0.00"), cart.getCurrentTotal());
    }

    @Test
    @DisplayName("Invalid Input: Invalid cart, items, or engine inputs throw expected exceptions")
    void testInvalidInputs_ThrowsExceptions() {
        assertThrows(IllegalArgumentException.class, () -> new Cart(Collections.emptyList()));
        assertThrows(IllegalArgumentException.class, () ->
                new CartItem("P1", "Shoes", "Nike", "Footwear", new BigDecimal("-100.00"), 1));
        assertThrows(IllegalArgumentException.class, () ->
                new BuyXGetCheapestFreeRule("Footwear", 1));

        PromotionEngine engine = new PromotionEngine();
        assertThrows(NullPointerException.class, () -> engine.addRule(null));
        assertThrows(NullPointerException.class, () -> engine.checkout(null));
    }
}
