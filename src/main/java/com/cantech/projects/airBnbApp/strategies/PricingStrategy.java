package com.cantech.projects.airBnbApp.strategies;

import com.cantech.projects.airBnbApp.entities.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
