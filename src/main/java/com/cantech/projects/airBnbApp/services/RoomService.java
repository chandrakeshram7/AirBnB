package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.HotelDTO;
import com.cantech.projects.airBnbApp.dtos.RoomDTO;
import com.cantech.projects.airBnbApp.entities.Room;

import java.util.List;

public interface RoomService {
    RoomDTO createNewRoom(Long hotelId, RoomDTO roomDTO);
    RoomDTO getRoomById(Long hotelId, Long id);
    RoomDTO updateRoomById(Long id, RoomDTO roomDTO);
    void deleteRoomById(Long id);
    List<RoomDTO> getAllRoomsByHotelId(Long hotelId);
}
