package internship.portfolio.jwt;

import internship.portfolio.common.ApiException;
import internship.portfolio.common.ExceptionEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    // JWT 서명을 위한 Key 객체 선언
    private final Key key;

    // 생성자 통해 Key 초기화. jwt.secret 가져와서 secretKey에 저장
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        // BASE64로 인코딩된 secretKey 디코딩
        byte[] keyBites = Decoders.BASE64.decode(secretKey);
        // 디코딩된 secretKey 이용해 Key 객체 생성
        this.key = Keys.hmacShaKeyFor(keyBites);
    }

    // 사용자 정보 이용해 AccessToken, RefreshToken 생성
    public JwtToken generateToken(Authentication authentication, String sessionId) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime(); // 현재 시각 가져오기
        Date issuedAt = new Date(); // 토큰 발급 시각 저장

        // AccessToken 생성
        String accessToken = Jwts.builder()
                .setHeader(createHeaders()) // Header 설정
                .setSubject("accessToken") // 토큰 주제 설정
                .claim("iss", "off") // 토큰 발급자 설정
                .claim("aud", authentication.getName()) // 토큰 대상자 설정
                .claim("auth", authorities) // 사용자 권한 설정
                .claim("sessionId", sessionId) // sessionId 추가
                .setExpiration(new Date(now + 1800000)) // 토큰 만료 시간 설정 (30분)
                .setIssuedAt(issuedAt) // 토큰 발급 시각 설정
                .signWith(key, SignatureAlgorithm.HS512) // 서명 알고리즘 설정
                .compact();

        // RefreshToken 생성
        String refreshToken = Jwts.builder()
                .setHeader(createHeaders()) // Header 설정
                .setSubject("refreshToken") // 토큰 주제 설정
                .claim("iss", "off") // 토큰 발급자 설정
                .claim("aud", authentication.getName()) // 토큰 대상자 설정
                .claim("auth", authorities) // 사용자 권한 설정
                .claim("add", "ref") // 추가 정보 설정
                .claim("sessionId", sessionId) // sessionId 추가
                .setExpiration(new Date(now + 604800000)) // 토큰 만료 시간 설정 (7일)
                .setIssuedAt(issuedAt) // 토큰 발급 시각 설정
                .signWith(key, SignatureAlgorithm.HS512) // 서명 알고리즘 설정
                .compact();

        // Token 객체 생성해 반환
        return JwtToken.builder()
                .grantType("Bearer") // 토큰 타입 설정
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 디코딩해 정보를 꺼낸 후 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token); // JWT 디코딩

        // 반환한 Claim 권한이 없다면 예외 발생
        if (claims.get("auth") == null) {
            throw new RuntimeException("Token without permission");
        }

        // 반환한 Claim에서 권한 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어 Authentication 반환
        // User : username, password, authorities 구성
        UserDetails principal = new User((String)claims.get("aud"), "", authorities);

        // 최종 Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // JWT 파싱해 유효성 검증
    public boolean validateToken(String token) {
        try {
            // 유효한 토큰일 경우 true 반환
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new ApiException(ExceptionEnum.INVALID_TOKEN); // 토큰이 잘못된 경우
        } catch (ExpiredJwtException e) {
            throw new ApiException(ExceptionEnum.TIMEOUT_TOKEN); // 토큰이 만료된 경우
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            throw new ApiException(ExceptionEnum.INVALID_TOKEN); // 지원하지 않는 토큰이거나 잘못된 형식의 경우
        } catch (Exception e){
            throw new ApiException(ExceptionEnum.INVALID_TOKEN); // 그 외의 경우
        }
    }

    // Header 내용 설정
    private static Map<String, Object> createHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS512"); // 비밀 키가 64byte 이므로 HS512 사용
        headers.put("typ", "JWT");
        return headers;
    }

    // JWT 파싱하여 Claim 정보 반환
    private Claims parseClaims(String accessToken) {
        try {
            // 유효한 토큰일 경우 파싱하여 인증에 사용할 Claim 정보 반환
            return Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료된 토큰의 경우 예외 객체에 포함된 Claim 정보 반환
        }
    }
}