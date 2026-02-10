package com.cantech.projects.airBnbApp.dtos;

import com.cantech.projects.airBnbApp.entities.HotelContactInfo;
import com.cantech.projects.airBnbApp.entities.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class HotelDTO {

    private Long id;

    private String name;

    private String city;

    private String[] photos;

    private String[] amenities;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private HotelContactInfo contactInfo;

    private Boolean active;

    private User owner;
}
