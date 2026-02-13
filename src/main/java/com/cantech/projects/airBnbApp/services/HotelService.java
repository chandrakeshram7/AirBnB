package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.HotelDTO;
import com.cantech.projects.airBnbApp.dtos.HotelInfoDTO;
import com.cantech.projects.airBnbApp.entities.Hotel;
import org.jspecify.annotations.Nullable;

public interface HotelService {
    HotelDTO createNewHotel(HotelDTO hotelDTO);
    HotelDTO getHotelById(Long id);
    HotelDTO updateHotelById(Long id, HotelDTO hotelDTO);
    void deleteHotelById(Long id);
    void activateHotel(Long id);
    HotelInfoDTO getHotelInfoByHotelId(Long hotelId);
}
