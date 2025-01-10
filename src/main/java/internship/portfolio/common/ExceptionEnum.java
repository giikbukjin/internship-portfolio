package internship.portfolio.common;

import lombok.Getter;
import lombok.ToString;

@Getter
public enum ExceptionEnum {
    INVALID_TOKEN("Invalid token"),
    TIMEOUT_TOKEN("Token expired"),
    UNKNOWN_ERROR("Unknown error");

    private final String message;

    ExceptionEnum(String message) {
        this.message = message;
    }
}