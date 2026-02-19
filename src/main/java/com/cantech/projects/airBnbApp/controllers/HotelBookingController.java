package com.cantech.projects.airBnbApp.controllers;

import com.cantech.projects.airBnbApp.dtos.BookingDTO;
import com.cantech.projects.airBnbApp.dtos.BookingRequestDTO;
import com.cantech.projects.airBnbApp.dtos.GuestDTO;
import com.cantech.projects.airBnbApp.services.BookingService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDTO> initialiseBooking(@RequestBody BookingRequestDTO bookingRequestDTO){
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequestDTO));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDTO> addGuests(@PathVariable Long bookingId,  @RequestBody List<GuestDTO> guests) throws AccessDeniedException {
        return ResponseEntity.ok(bookingService.addGuests(bookingId, guests));
    }
}

