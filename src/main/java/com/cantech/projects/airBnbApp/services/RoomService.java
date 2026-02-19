package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.HotelDTO;
import com.cantech.projects.airBnbApp.dtos.RoomDTO;
import com.cantech.projects.airBnbApp.entities.Room;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface RoomService {
    RoomDTO createNewRoom(Long hotelId, RoomDTO roomDTO);
    RoomDTO getRoomById(Long hotelId, Long id);
    RoomDTO updateRoomById(Long id, RoomDTO roomDTO) throws AccessDeniedException;
    void deleteRoomById(Long id) throws AccessDeniedException;
    List<RoomDTO> getAllRoomsByHotelId(Long hotelId);
}
