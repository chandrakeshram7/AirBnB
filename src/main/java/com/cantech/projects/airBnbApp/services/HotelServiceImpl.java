package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.HotelDTO;
import com.cantech.projects.airBnbApp.entities.Hotel;
import com.cantech.projects.airBnbApp.entities.HotelContactInfo;
import com.cantech.projects.airBnbApp.exceptions.ResourceNotFoundException;
import com.cantech.projects.airBnbApp.repositories.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;


    @Override
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
    public void deleteHotelById(Long id) {
        boolean exists = hotelRepository.existsById(id);
        if(!exists){
            throw new ResourceNotFoundException("Hotel with id "+id +" does not exist");
        }
        log.info("Deleting the hotel with id {}", id);
        hotelRepository.deleteById(id);
        log.info("Deleted the hotel with id {}", id);

        //TODO Need to delete the related inventory as well

    }

    @Override
    public void activateHotel(Long id) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel does not exists with id "+id));
        log.info("Activating the hotel with hotel id {}", id);
        hotel.setActive(true);
        //TODO Need to create the inventory once the hotel is activated
        hotelRepository.save(hotel);
        log.info("Activated the hotel with hotel id {}", id);
    }


}
