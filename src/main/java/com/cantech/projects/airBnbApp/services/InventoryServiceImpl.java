package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.HotelDTO;
import com.cantech.projects.airBnbApp.dtos.HotelSearchRequestDTO;
import com.cantech.projects.airBnbApp.entities.Hotel;
import com.cantech.projects.airBnbApp.entities.Inventory;
import com.cantech.projects.airBnbApp.entities.Room;
import com.cantech.projects.airBnbApp.repositories.HotelMinPriceRepository;
import com.cantech.projects.airBnbApp.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final ModelMapper modelMapper;
    private final static long  DAYS_THRESHOLD = 90;

    @Override
    public void initializeRoomForYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for(; !today.isAfter(endDate); today=today.plusDays(1)){
            Inventory inventory = Inventory
                    .builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .bookedCount(0)
                    .reservedCount(0)
                    .closed(false)
                    .build();
            System.out.println("I am executing");
            inventoryRepository.save(inventory);
        }

    }

    @Override
    public void deleteInventories(Room room) {
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<?> searchHotels(HotelSearchRequestDTO hotelSearchRequestDTO) {
        long dateCount = ChronoUnit.DAYS.between(hotelSearchRequestDTO.getStartDate(), hotelSearchRequestDTO.getEndDate())+1;
        long noOfDays = ChronoUnit.DAYS.between(
                LocalDate.now(),
                hotelSearchRequestDTO.getStartDate()
        );

        Pageable pageable = PageRequest.of(hotelSearchRequestDTO.getPage(), hotelSearchRequestDTO.getSize());
        if(noOfDays > DAYS_THRESHOLD){
            Page<Hotel> hotelPage = inventoryRepository.findHotelsWithAvailableInventory(
                    hotelSearchRequestDTO.getCity(),
                    hotelSearchRequestDTO.getStartDate(),
                    hotelSearchRequestDTO.getEndDate(),
                    hotelSearchRequestDTO.getRoomsCount(),
                    dateCount,
                    pageable
            );

            return hotelPage.map(hotel -> modelMapper.map(hotel, HotelDTO.class));
        }

        return hotelMinPriceRepository.findHotelsWithAvailableInventory(
                hotelSearchRequestDTO.getCity(),
                hotelSearchRequestDTO.getStartDate(),
                hotelSearchRequestDTO.getEndDate(),
                pageable
        );



    }
}
