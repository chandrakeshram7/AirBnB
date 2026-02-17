package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.HotelDTO;
import com.cantech.projects.airBnbApp.dtos.HotelSearchRequestDTO;
import com.cantech.projects.airBnbApp.entities.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForYear(Room room);
    void deleteInventories(Room room);

    Page<?> searchHotels(HotelSearchRequestDTO hotelSearchRequestDTO);
}
