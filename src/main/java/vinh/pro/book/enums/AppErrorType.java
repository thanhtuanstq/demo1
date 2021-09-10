package vinh.pro.book.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum AppErrorType {
    // Common error
    UNKNOWN(false, "CMN_500", "internal server error"),
    BAD_REQUEST(false, "CMN_400", "bad client request"),
    UNAUTHORIZED(false, "CMN_401", "unauthorized request"),
    ACCESS_DENIED(false, "CMN_403", "access denied request"),
    RESOURCE_NOT_FOUND(false, "ERR_4404", "resource not found: resource_name = %s, field_name = %s, field_value = %s"),

    // Others error

    ;
    private final Boolean success;
    private final String code;
    private final String message;

    AppErrorType(Boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
