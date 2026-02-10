package com.cantech.projects.airBnbApp.controllers;

import com.cantech.projects.airBnbApp.dtos.RoomDTO;
import com.cantech.projects.airBnbApp.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDTO> createNewRoom(@PathVariable Long hotelId,  @RequestBody RoomDTO roomDTO){
        return new ResponseEntity<>(roomService.createNewRoom(hotelId, roomDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRoomsOfHotel(@PathVariable Long hotelId){
        return new ResponseEntity<>(roomService.getAllRoomsByHotelId(hotelId), HttpStatus.FOUND);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId){
        return new ResponseEntity<>(roomService.getRoomById(hotelId, roomId), HttpStatus.FOUND);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDTO> updateRoomById(@PathVariable Long roomId, @RequestBody RoomDTO roomDTO){
        return new ResponseEntity<>(roomService.updateRoomById(roomId, roomDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoomById(@PathVariable Long roomId){
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }



}
