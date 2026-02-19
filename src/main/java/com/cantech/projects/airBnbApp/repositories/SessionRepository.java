package com.cantech.projects.airBnbApp.repositories;

import com.cantech.projects.airBnbApp.entities.Session;
import com.cantech.projects.airBnbApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByRefreshToken(String token);

    List<Session> findByUser(User user);
}
