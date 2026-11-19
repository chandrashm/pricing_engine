package com.myntra.promo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PromotionEngine {
    private final List<PromotionRule> rules = new ArrayList<>();

    public PromotionEngine addRule(PromotionRule rule) {
        Objects.requireNonNull(rule, "Promotion rule cannot be null.");
        rules.add(rule);
        return this;
    }

    public void checkout(Cart cart) {
        Objects.requireNonNull(cart, "Cart cannot be null.");
        for (PromotionRule rule : rules) {
            rule.apply(cart);
        }
    }
}
