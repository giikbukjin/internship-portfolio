package internship.portfolio.session.service;

import internship.portfolio.session.entity.Session;
import internship.portfolio.session.repository.SessionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DBSessionStoreService implements SessionStoreService {
    private final SessionRepository sessionRepository;

    public DBSessionStoreService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void saveSession(String sessionId, String username) {
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(2);
        Session session = new Session();

        session.setSessionId(sessionId);
        session.setUsername(username);
        session.setExpiresAt(expiresAt);

        sessionRepository.save(session);
    }

    @Override
    public Session getSession(String sessionId) {
        return sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sessionId"));
    }

    @Override
    public boolean isSessionValid(Session session) {
        // 만료일이 현재보다 과거라면 세션이 만료된 것
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            System.out.println("FALSE");
            return false;
        } else {
            System.out.println("TRUE");
            return true;
        }
    }

    @Override
    public void deleteSession(String sessionId) {
        sessionRepository.deleteBySessionId(sessionId);
    }
}
