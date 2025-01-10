package internship.portfolio.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    private String grantType; // JWT 인증 유형
    private String accessToken;
    private String refreshToken;
}