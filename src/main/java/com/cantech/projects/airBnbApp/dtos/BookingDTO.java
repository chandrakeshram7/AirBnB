package com.cantech.projects.airBnbApp.dtos;

import com.cantech.projects.airBnbApp.entities.Guest;
import com.cantech.projects.airBnbApp.entities.Hotel;
import com.cantech.projects.airBnbApp.entities.Room;
import com.cantech.projects.airBnbApp.entities.User;
import com.cantech.projects.airBnbApp.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {

    private Long id;

    private Integer roomsCount;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BookingStatus bookingStatus;

    private Set<GuestDTO> guests;
}
