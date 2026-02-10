package com.cantech.projects.airBnbApp.repositories;

import com.cantech.projects.airBnbApp.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
