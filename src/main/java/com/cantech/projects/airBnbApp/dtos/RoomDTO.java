package com.cantech.projects.airBnbApp.dtos;

import com.cantech.projects.airBnbApp.entities.Hotel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomDTO {

    private Long id;

//    private Hotel hotel;    We don't need hotel info from Room

    private String type;

    private BigDecimal basePrice;

    private String[] photos;

    private String[] amenities;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer totalCount;

    private Integer capacity;
}
