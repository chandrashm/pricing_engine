package com.myntra.promo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {
    private final List<CartItem> items;
    private BigDecimal currentTotal;
    private final List<String> appliedDiscounts;

    public Cart(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty at checkout.");
        }
        this.items = new ArrayList<>(items);
        this.currentTotal = calculateOriginalTotal();
        this.appliedDiscounts = new ArrayList<>();
    }

    public BigDecimal calculateOriginalTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getTotalPrice());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public void applyDiscount(String ruleName, BigDecimal discountAmount) {
        if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        BigDecimal actualDiscount = discountAmount.min(this.currentTotal).setScale(2, RoundingMode.HALF_UP);
        this.currentTotal = this.currentTotal.subtract(actualDiscount);
        this.appliedDiscounts.add(ruleName + ": -Rs. " + actualDiscount);
    }

    public List<CartItem> getItems() { return Collections.unmodifiableList(items); }
    public BigDecimal getCurrentTotal() { return currentTotal; }
    public List<String> getAppliedDiscounts() { return Collections.unmodifiableList(appliedDiscounts); }
}
