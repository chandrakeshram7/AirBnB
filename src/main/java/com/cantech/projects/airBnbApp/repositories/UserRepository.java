package com.cantech.projects.airBnbApp.repositories;

import com.cantech.projects.airBnbApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
