package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.HotelDTO;
import com.cantech.projects.airBnbApp.dtos.HotelInfoDTO;
import com.cantech.projects.airBnbApp.entities.Hotel;
import com.cantech.projects.airBnbApp.entities.HotelContactInfo;
import com.cantech.projects.airBnbApp.entities.Room;
import com.cantech.projects.airBnbApp.exceptions.ResourceNotFoundException;
import com.cantech.projects.airBnbApp.repositories.HotelRepository;
import com.cantech.projects.airBnbApp.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomService roomService;

    @Override
    @Transactional
    public HotelDTO createNewHotel(HotelDTO hotelDTO){
        log.info("Creating a new hotel with name as {}", hotelDTO.getName());
        Hotel hotel= modelMapper.map(hotelDTO, Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with name as {}", hotelDTO.getName());
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO getHotelById(Long id){
        log.info("Getting the hotel with id {}", id);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->  new ResourceNotFoundException("No hotel with provided id exists "));
        log.info("Got the hotel with the id {}", id);
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO updateHotelById(Long id, HotelDTO hotelDTO) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No hotel with id "+ id +" is present"));
        modelMapper.map(hotelDTO, hotel);
        hotel.setId(id);
        hotelDTO.setCreatedAt(hotel.getCreatedAt());
        hotelDTO.setId(hotel.getId());
        log.info("Updating the hotel with id {}", id);
        hotelRepository.save(hotel);
        log.info("Updated the hotel with id {}", id);
        return hotelDTO;
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        log.info("Deleting the hotel with id {}", id);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Hotel is not found with id "+id));
        List<Room> rooms = hotel.getRooms();
        for(Room room : rooms){
            inventoryService.deleteInventories(room);
            roomService.deleteRoomById(room.getId());
        }
        hotelRepository.delete(hotel);
        log.info("Deleted the hotel with id {}", id);

    }

    @Override
    @Transactional
    public void activateHotel(Long id) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel does not exists with id "+id));
        log.info("Activating the hotel with hotel id {}", id);
        hotel.setActive(true);
        for(Room room : hotel.getRooms()){
            inventoryService.initializeRoomForYear(room);
        }
        log.info("Activated the hotel with hotel id {}", id);
    }

    @Override
    public HotelInfoDTO getHotelInfoByHotelId(Long hotelId) {
        return null;
    }


}
