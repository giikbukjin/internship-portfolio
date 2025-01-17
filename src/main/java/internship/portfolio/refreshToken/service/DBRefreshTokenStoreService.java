package internship.portfolio.refreshToken.service;

import internship.portfolio.common.ApiException;
import internship.portfolio.common.ExceptionEnum;
import internship.portfolio.refreshToken.entity.RefreshToken;
import internship.portfolio.refreshToken.repository.RefreshTokenRepository;
import org.springframework.stereotype.Component;

@Component
public class DBRefreshTokenStoreService implements RefreshTokenStoreService {
    private final RefreshTokenRepository refreshTokenRepository;

    public DBRefreshTokenStoreService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void saveRefreshToken(String token, String username) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setRefreshToken(token);
        refreshToken.setUsername(username);

        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ApiException(ExceptionEnum.INVALID_TOKEN));
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }
}
