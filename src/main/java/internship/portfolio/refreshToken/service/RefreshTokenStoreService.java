package internship.portfolio.refreshToken.service;

import internship.portfolio.refreshToken.entity.RefreshToken;

public interface RefreshTokenStoreService {
    void saveRefreshToken(String refreshToken, String username);

    RefreshToken getRefreshToken(String refreshToken);

    void deleteRefreshToken(String refreshToken);
}
