package com.cantech.projects.airBnbApp.services;

import com.cantech.projects.airBnbApp.dtos.BookingDTO;
import com.cantech.projects.airBnbApp.dtos.BookingRequestDTO;
import com.cantech.projects.airBnbApp.dtos.GuestDTO;
import com.cantech.projects.airBnbApp.entities.*;
import com.cantech.projects.airBnbApp.enums.BookingStatus;
import com.cantech.projects.airBnbApp.enums.Role;
import com.cantech.projects.airBnbApp.exceptions.ResourceNotFoundException;
import com.cantech.projects.airBnbApp.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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


        //Demo user
        User user = new User();
        if(saver==0){
            user.setRoles(Set.of(Role.HOTEL_MANAGER));
            user.setName("Chandrakesh");
            user.setEmail("chandrakeshram@gmail.com");
            user.setPassword("password");
            userRepository.save(user);
            saver++;
        }
        user = userRepository.findById(1L).orElseThrow(()-> new ResourceNotFoundException("User not found with id "+ 1L)); //TODO Remove DEMO user

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
    public BookingDTO addGuests(Long bookingId, List<GuestDTO> guests) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                ()-> new ResourceNotFoundException("No such booking available with id "+ bookingId)
        );

        if(isBookingExpired(booking)){
            throw new IllegalStateException("Booking has already expired");
        }

        if(booking.getBookingStatus()!= BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under reserved state, cannot add guests");
        }

        User userCreator = getCurrentUser();
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
        User user = new User();  //TODO Remove the dummy user logic
        user.setPassword("pass");
        user.setRoles(Set.of(Role.GUEST));
        user.setName("chandra");
        user.setEmail("chandrakesher@gmail.com");
        return user = userRepository.save(user);
    }
}
