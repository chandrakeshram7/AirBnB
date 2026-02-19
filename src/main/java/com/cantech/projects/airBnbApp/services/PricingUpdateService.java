package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.entities.Hotel;
import com.cantech.projects.airBnbApp.entities.HotelMinPrice;
import com.cantech.projects.airBnbApp.entities.Inventory;
import com.cantech.projects.airBnbApp.repositories.HotelMinPriceRepository;
import com.cantech.projects.airBnbApp.repositories.HotelRepository;
import com.cantech.projects.airBnbApp.repositories.InventoryRepository;
import com.cantech.projects.airBnbApp.strategies.PricingService;
import com.cantech.projects.airBnbApp.strategies.PricingStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PricingUpdateService {

    private final HotelRepository hotelRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final InventoryRepository inventoryRepository;
    private final PricingService pricingService;

    //Scheduler to update the inventory and HotelMinPrice tables every hour

    @Scheduled(cron = "*/5 * * * * *")
    public void updatePrices(){
        int page  = 0;
        int batchSize = 100;

        while(true){
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
            if(hotelPage.isEmpty()){
                break;
            }

            hotelPage.getContent().forEach(this::updateHotelPrices);

            page++;
        }

    }

    private void updateHotelPrices(Hotel hotel){
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);
        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate);
        updateInventoryPrices(inventoryList);
        updateHotelPrices(hotel, inventoryList, startDate, endDate);
    }

    private void updateHotelPrices(Hotel hotel, List<Inventory> inventoryList,LocalDate startDate,LocalDate endDate){
        Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO)));

        //Prepare Hotel entities in bulk
        List<HotelMinPrice>hotelPrices = new ArrayList<>();
        dailyMinPrices.forEach((date, price) -> {
            HotelMinPrice hotelMinPrice = hotelMinPriceRepository.findByHotelAndDate(hotel, date)
                    .orElse(new HotelMinPrice(hotel, date));
            hotelMinPrice.setPrice(price);
            hotelPrices.add(hotelMinPrice);
        });

        hotelMinPriceRepository.saveAll(hotelPrices);
    }

    private void updateInventoryPrices(List<Inventory> inventoryList){
        inventoryList.forEach(
                inventory -> {
                    BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
                    inventory.setPrice(dynamicPrice);
                }
        );
        inventoryRepository.saveAll(inventoryList);
    }
}
