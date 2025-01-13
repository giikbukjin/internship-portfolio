package internship.portfolio.common;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ExceptionEnum exceptionEnum;

    public ApiException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.exceptionEnum = exceptionEnum;
    }

    public ApiException(ExceptionEnum exceptionEnum, Throwable cause) {
        super(exceptionEnum.getMessage(), cause);
        this.exceptionEnum = exceptionEnum;
    }
}