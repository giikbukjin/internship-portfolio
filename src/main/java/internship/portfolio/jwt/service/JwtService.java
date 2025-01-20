package internship.portfolio.jwt.service;

import internship.portfolio.common.ApiException;
import internship.portfolio.common.ExceptionEnum;
import internship.portfolio.jwt.JwtToken;
import internship.portfolio.jwt.JwtTokenProvider;
import internship.portfolio.session.entity.Session;
import internship.portfolio.session.repository.SessionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SessionRepository sessionRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtService(SessionRepository sessionRepository, JwtTokenProvider jwtTokenProvider) {
        this.sessionRepository = sessionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public JwtToken reissueAccessToken(String sessionId, String refreshToken, Authentication authentication) {
        // 받은 SessionID가 DB에 존재하는지 확인
        Session session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.INVALID_SESSION));

        // Header와 DB의 RefreshToken이 일치하는지 검증
        if (jwtTokenProvider.validateToken(refreshToken) && refreshToken.equals(session.getRefreshToken())) {
            return reissueTokenFromSession(session, authentication, refreshToken);
        }
        throw new ApiException(ExceptionEnum.INVALID_TOKEN);
    }

    private JwtToken reissueTokenFromSession(Session session, Authentication authentication, String refreshToken) {
        // AccessToken 생성
        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication, session.getSessionId());

        // JwtToken 객체 생성 후 반환
        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
}