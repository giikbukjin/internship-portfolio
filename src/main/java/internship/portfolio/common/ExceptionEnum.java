package internship.portfolio.common;

import lombok.Getter;
import lombok.ToString;

@Getter
public enum ExceptionEnum {
    INVALID_TOKEN("Invalid token"),
    TIMEOUT_TOKEN("Token expired"),
    UNKNOWN_TOKEN_ERROR("Unknown token error"),

    INVALID_SESSION("Session not found"),
    UNKNOWN_SESSION_ERROR("Unknown session error"),
    ;

    private final String message;

    ExceptionEnum(String message) {
        this.message = message;
    }
}