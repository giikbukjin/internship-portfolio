package internship.portfolio.member.service;

import internship.portfolio.jwt.JwtToken;
import internship.portfolio.jwt.JwtTokenProvider;
import internship.portfolio.session.service.SessionStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SessionStoreService sessionService;

    // 사용자 로그인
    public JwtToken signIn(String username, String password) {
        String sessionId = UUID.randomUUID().toString(); // sessionId 생성

        // username, password 이용해 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // authenticate() 통해 요청한 Member 검증 진행
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보와 sessionId 기반으로 JWT 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, sessionId);

        // 생성된 sessionId, refreshToken을 저장소에 저장
        sessionService.saveSession(sessionId, authentication.getName(), jwtToken.getRefreshToken());

        return jwtToken;
    }
}