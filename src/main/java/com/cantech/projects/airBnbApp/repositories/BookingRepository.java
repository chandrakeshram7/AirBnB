package com.cantech.projects.airBnbApp.repositories;

import com.cantech.projects.airBnbApp.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
