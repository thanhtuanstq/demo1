package vinh.pro.book.payload.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import vinh.pro.book.utils.DateTimeUtils;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse
        implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SPLIT_SIGN = "_";

    private Boolean success;
    private String code;
    private String message;
    private String timestamp = DateTimeUtils.getISOLocalDateTime();
    private Object data;

    public ApiResponse(String serviceCode, HttpStatus code, String message, Object data) {
        this.success = true;
        this.code = serviceCode + SPLIT_SIGN + code.value();
        this.message = message;
        this.data = data;
    }
}
