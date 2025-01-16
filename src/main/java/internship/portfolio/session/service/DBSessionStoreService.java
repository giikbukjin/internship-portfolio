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
    public void deleteSession(String sessionId) {
        sessionRepository.deleteBySessionId(sessionId);
    }
}
