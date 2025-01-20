package internship.portfolio.jwt.controller;

import internship.portfolio.common.ApiException;
import internship.portfolio.common.ExceptionEnum;
import internship.portfolio.jwt.JwtToken;
import internship.portfolio.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
@Slf4j
public class JwtController {
    private final JwtService jwtService;

    @PostMapping("/reissue/access-token")
    public ResponseEntity<JwtToken> reissueAccessToken(
            @RequestHeader("refreshToken") String refreshToken,
            @RequestBody Map<String, String> requestBody // JSON을 Map으로 받아 SessionId 값 추출
    ) {
        // Map에서 sessionId를 String 형식으로 추출
        String sessionId = requestBody.get("sessionId");

        // 현재 SecurityContext에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // AccessToken 재발급
        JwtToken jwtToken = jwtService.reissueAccessToken(sessionId, refreshToken, authentication);

        return ResponseEntity.ok(jwtToken);
    }
}