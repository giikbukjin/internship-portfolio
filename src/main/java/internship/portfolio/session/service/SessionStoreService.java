package internship.portfolio.session.service;

import internship.portfolio.session.entity.Session;

public interface SessionStoreService {
    void saveSession(String sessionId, String username);

    Session getSession(String sessionId);

    void deleteSession(String sessionId);
}