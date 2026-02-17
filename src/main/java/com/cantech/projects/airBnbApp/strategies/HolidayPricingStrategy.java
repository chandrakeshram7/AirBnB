package com.cantech.projects.airBnbApp.strategies;

import com.cantech.projects.airBnbApp.entities.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy {

    private final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = pricingStrategy.calculatePrice(inventory);
        boolean isHoliday = true;  //TODO Here can implement a logic for finding the holiday
        if(isHoliday){
            price = price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}
