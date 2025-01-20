package internship.portfolio.session.service;

import internship.portfolio.session.entity.Session;

public interface SessionStoreService {
    void saveSession(String sessionId, String username, String refreshToken);

    Session getSession(String sessionId);

    void deleteSession(String sessionId);

    Session getRefreshToken(String refreshToken);

    void deleteRefreshToken(String refreshToken);
}