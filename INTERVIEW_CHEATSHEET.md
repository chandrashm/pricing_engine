# Publicis Sapient Manager Interview Cheat Sheet (LLD + Coding + Testing)

## 1. 60-Minute Time Split
- **0-7 min (Clarify):** Confirm stacking rules, ordering priority, item-level vs cart-level tracking, and edge cases (empty cart, negative prices, zero floor, BigDecimal).
- **7-15 min (Blueprint):** Walk through `CartItem`, `Cart`, `PromotionRule` interface, concrete rules, and `PromotionEngine` (Pipeline / Chain of Responsibility + Open/Closed Principle). State `O(R * N)` time and `O(1)` extra space.
- **15-40 min (Code):** Build from inside out (`CartItem` -> `Cart` -> `PromotionRule` -> concrete rules -> `PromotionEngine` -> `Main`).
- **40-52 min (Test):** Demonstrate isolated rule unit tests, chained integration tests, boundary tests (`0.00` floor), and invalid input exception tests.
- **52-60 min (Scale & Enhance):** Mention DB-driven rule config, mutual exclusivity / short-circuiting, best-deal selector, and item-level proration for partial returns.

## 2. 2-Minute Spoken Walkthrough
> "To solve this cleanly, I modeled the checkout discount system as a composable rule pipeline—similar to an assembly line where the shopping cart passes through a sequence of independent promotion rules.
> 1. CartItem & Cart: CartItem holds product metadata; Cart holds items, tracks the running total using BigDecimal, guards against negative totals, and keeps an audit receipt of applied discounts.
> 2. PromotionRule Interface: Every rule implements `apply(Cart cart)`, making each rule completely independent and unit-testable.
> 3. Concrete Rules: BrandDiscountRule, ComboDiscountRule, CartThresholdDiscountRule, and BuyXGetCheapestFreeRule each encapsulate one promotion policy.
> 4. PromotionEngine: Runs the cart through an ordered list of rules so the output of Rule 1 feeds into Rule 2.
> This adheres to Single Responsibility and Open/Closed Principles—new promotions plug in without touching existing code—and runs in O(R * N) time and O(1) extra space."
