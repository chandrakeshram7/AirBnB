package com.cantech.projects.airBnbApp.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Setter;

@Setter
@Embeddable
public class HotelContactInfo {

    private String address;

    private String phoneNumber;

    private String email;

    private String location;

}
