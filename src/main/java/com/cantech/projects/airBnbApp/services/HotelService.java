package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.HotelDTO;
import com.cantech.projects.airBnbApp.dtos.HotelInfoDTO;
import com.cantech.projects.airBnbApp.entities.Hotel;
import org.jspecify.annotations.Nullable;

import java.nio.file.AccessDeniedException;

public interface HotelService {
    HotelDTO createNewHotel(HotelDTO hotelDTO);
    HotelDTO getHotelById(Long id) throws AccessDeniedException;
    HotelDTO updateHotelById(Long id, HotelDTO hotelDTO) throws AccessDeniedException;
    void deleteHotelById(Long id) throws AccessDeniedException;
    void activateHotel(Long id) throws AccessDeniedException;
    HotelInfoDTO getHotelInfoByHotelId(Long hotelId);
}
