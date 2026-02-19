package com.cantech.projects.airBnbApp.security;

import com.cantech.projects.airBnbApp.entities.Session;
import com.cantech.projects.airBnbApp.entities.User;
import com.cantech.projects.airBnbApp.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    @Value("${SESSION_LIMIT}")
    private Integer SESSION_LIMIT;

    private final JwtService jwtService;
    private final SessionRepository sessionRepository;
    public void createSession(User user){
        String refreshToken = jwtService.getRefreshToken(user);

        List<Session> sessions = sessionRepository.findByUser(user);

        if(sessions.size() >= SESSION_LIMIT){
            sessions.sort(Comparator.comparing(Session::getLastUpdatedAt));

            Session session = sessions.get(0);

            sessionRepository.delete(session);
        }

        Session newSession = new Session();
        newSession.setUser(user);
        newSession.setRefreshToken(refreshToken);
        sessionRepository.save(newSession);

    }


    public void validateSession(String refreshToken){
        Session session = sessionRepository.findByRefreshToken(refreshToken).orElse(null);
        if(session==null){
            throw new RuntimeException("Cannot validate the session");
        }
        session.setLastUpdatedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

}
