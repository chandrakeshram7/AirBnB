package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.BookingDTO;
import com.cantech.projects.airBnbApp.dtos.BookingRequestDTO;
import com.cantech.projects.airBnbApp.dtos.GuestDTO;
import com.cantech.projects.airBnbApp.entities.*;
import com.cantech.projects.airBnbApp.enums.BookingStatus;
import com.cantech.projects.airBnbApp.enums.Role;
import com.cantech.projects.airBnbApp.exceptions.ResourceNotFoundException;
import com.cantech.projects.airBnbApp.exceptions.UnAuthorisedException;
import com.cantech.projects.airBnbApp.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final GuestRepository guestRepository;
    static int saver = 0;
    @Override
    @Transactional
    public BookingDTO initialiseBooking(BookingRequestDTO bookingRequestDTO) {
        Hotel hotel = hotelRepository.findById(bookingRequestDTO.getHotelId())
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with Id "+ bookingRequestDTO.getHotelId()));
        Room room = roomRepository.findById(bookingRequestDTO.getRoomId())
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with Id "+ bookingRequestDTO.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockInventory(room.getId(), bookingRequestDTO.getCheckInDate(), bookingRequestDTO.getCheckOutDate(), bookingRequestDTO.getRoomsCount());

        Long daysCount = ChronoUnit.DAYS.between(bookingRequestDTO.getCheckInDate(),bookingRequestDTO.getCheckOutDate())+1;

        if(inventoryList.size()!= daysCount){
            throw new IllegalStateException(("Room is not available anymore"));
        }

        //Reserve the rooms and update the booked count
        for(Inventory inventory : inventoryList){
            inventory.setReservedCount(inventory.getReservedCount()+ bookingRequestDTO.getRoomsCount());
        }

        inventoryRepository.saveAll(inventoryList);

        User user = getCurrentUser();

        //TODO : Calculate dynamic pricing

        //Create a Booking object
        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequestDTO.getCheckInDate())
                .checkOutDate(bookingRequestDTO.getCheckOutDate())
                .user(user)
                .roomsCount(bookingRequestDTO.getRoomsCount())
                .amount(BigDecimal.TEN)
                .build();
        booking = bookingRepository.save(booking);

        return modelMapper.map(booking, BookingDTO.class);

    }

    @Override
    public BookingDTO addGuests(Long bookingId, List<GuestDTO> guests) throws AccessDeniedException{
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                ()-> new ResourceNotFoundException("No such booking available with id "+ bookingId)
        );

        User userCreator = getCurrentUser();
        if(!userCreator.equals(booking.getUser())){
            throw new AccessDeniedException("Cannot add guests to the booking that belongs to someone else");
        }

        if(isBookingExpired(booking)){
            throw new RuntimeException("Booking has already expired");
        }

        if(booking.getBookingStatus()!= BookingStatus.RESERVED){
            throw new RuntimeException("Booking is not under reserved state, cannot add guests");
        }

        for(GuestDTO guestDTo : guests ){
            Guest guest = modelMapper.map(guestDTo ,Guest.class);
            guest.setUser(userCreator);
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);
        }
        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDTO.class);

    }

    public boolean isBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser(){
        if(SecurityContextHolder.getContext().getAuthentication() == null){
            throw new UnAuthorisedException("User not authorized");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (User) authentication.getPrincipal();
    }
}
