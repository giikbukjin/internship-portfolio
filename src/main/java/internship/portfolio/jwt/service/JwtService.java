package internship.portfolio.jwt.service;

import internship.portfolio.jwt.JwtTokenProvider;
import internship.portfolio.session.repository.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SessionRepository sessionRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtService(SessionRepository sessionRepository, JwtTokenProvider jwtTokenProvider) {
        this.sessionRepository = sessionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
}
