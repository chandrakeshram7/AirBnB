package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.HotelDTO;
import com.cantech.projects.airBnbApp.dtos.RoomDTO;
import com.cantech.projects.airBnbApp.entities.Hotel;
import com.cantech.projects.airBnbApp.entities.Room;
import com.cantech.projects.airBnbApp.exceptions.ResourceNotFoundException;
import com.cantech.projects.airBnbApp.repositories.HotelRepository;
import com.cantech.projects.airBnbApp.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public RoomDTO createNewRoom(Long hotelId, RoomDTO roomDTO) {
        log.info("Creating a new room in the hotel with id {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel is not found with id "+ hotelId));
        Room room = modelMapper.map(roomDTO, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);
        log.info("Created a new room in the hotel with id {}", hotelId);

        //TODO Create inventory once room is created
        if(hotel.getActive()){
            inventoryService.initializeRoomForYear(room);
        }
        return modelMapper.map(room ,RoomDTO.class);
    }

    @Override
    @Transactional
    public RoomDTO getRoomById(Long hotelId, Long id) {
        log.info("Getting the room with the id {}", id);
        Room room = roomRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Room is not found with id "+ id));
        return modelMapper.map(room, RoomDTO.class);
    }

    @Override
    public RoomDTO updateRoomById(Long id, RoomDTO roomDTO) {
        boolean exists = roomRepository.existsById(id);
        log.info("Updating the room with the id {}", id);
        if(!exists){
            throw new ResourceNotFoundException("Room is not found with id "+ id) ;
        }
        Room room = roomRepository.findById(id).orElse(null);
        Hotel hotel = room.getHotel();
        modelMapper.map(roomDTO, room);
        room.setId(id);
        room.setHotel(hotel);
        room = roomRepository.save(room);
        log.info("Updated the room with the id {}", id);
        return modelMapper.map(room , RoomDTO.class) ;
    }

    @Override
    @Transactional
    public void deleteRoomById(Long id) {
        Room room = roomRepository.findById(id).orElse(null);
        log.info("Deleting the room with the id {}", id);
        if(room == null){
            throw new ResourceNotFoundException("Room is not found with id "+ id) ;
        }
        inventoryService.deleteInventories(room);
        roomRepository.delete(room);
        log.info("Deleted the room with the id {}", id);
    }

    @Override
    @Transactional
    public List<RoomDTO> getAllRoomsByHotelId(Long hotelId) {
        log.info("Getting all the rooms of hotel with id {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel is not found with id "+ hotelId));
        return hotel
                .getRooms()
                .stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .toList();
    }
}
