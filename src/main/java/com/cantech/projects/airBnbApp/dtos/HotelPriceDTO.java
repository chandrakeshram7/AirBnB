package com.cantech.projects.airBnbApp.dtos;

import com.cantech.projects.airBnbApp.entities.Hotel;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDTO {

    private Hotel hotel;

    private Double price;  //Cheapest room price of hotel

}
