package com.cantech.projects.airBnbApp.strategies;

import com.cantech.projects.airBnbApp.entities.Inventory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public class BasePriceStrategy implements PricingStrategy{
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }

}
