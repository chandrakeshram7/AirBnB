package com.cantech.projects.airBnbApp.dtos;

import com.cantech.projects.airBnbApp.entities.User;
import com.cantech.projects.airBnbApp.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestDTO {

    private Long id;

    private User user ;

    private String name;

    private Gender gender;

    private Integer age;

}
