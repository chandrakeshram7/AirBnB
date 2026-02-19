package com.cantech.projects.airBnbApp.controllers;

import com.cantech.projects.airBnbApp.dtos.HotelDTO;
import com.cantech.projects.airBnbApp.services.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDTO> createNewHotel(@RequestBody HotelDTO hotelDTO){
        log.info("Attempting to create a new hotel with name {}", hotelDTO.getName());
        return new ResponseEntity<>(hotelService.createNewHotel(hotelDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long id) throws AccessDeniedException {
        log.info("Fetching the hotel with id {}", id);
        return new ResponseEntity<>(hotelService.getHotelById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDTO> updateHotelById(@PathVariable Long id, @RequestBody HotelDTO hotelDTO) throws AccessDeniedException{
        return new ResponseEntity<>(hotelService.updateHotelById(id, hotelDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotelById(@PathVariable Long id) throws AccessDeniedException{
        hotelService.deleteHotelById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> activateHotelById(@PathVariable Long id) throws AccessDeniedException{
        hotelService.activateHotel(id);
        return ResponseEntity.noContent().build();
    }


}
