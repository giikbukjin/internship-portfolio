package internship.portfolio.session.service;

import internship.portfolio.common.ApiException;
import internship.portfolio.common.ExceptionEnum;
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
        Session session = new Session();

        session.setSessionId(sessionId);
        session.setUsername(username);

        sessionRepository.save(session);
    }

    @Override
    public Session getSession(String sessionId) {
        return sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.INVALID_SESSION));
    }

    @Override
    public void deleteSession(String sessionId) {
        sessionRepository.deleteBySessionId(sessionId);
    }
}
