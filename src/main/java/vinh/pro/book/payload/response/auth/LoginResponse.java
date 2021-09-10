package vinh.pro.book.payload.response.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class LoginResponse
        implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String tokenType = "Bearer";

    public LoginResponse() {
    }

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
