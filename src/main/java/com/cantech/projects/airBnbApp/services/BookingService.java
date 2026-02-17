package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.BookingDTO;
import com.cantech.projects.airBnbApp.dtos.BookingRequestDTO;
import com.cantech.projects.airBnbApp.dtos.GuestDTO;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface BookingService {

    BookingDTO initialiseBooking(BookingRequestDTO bookingRequestDTO);

    BookingDTO addGuests(Long bookingId, List<GuestDTO> guests);
}
