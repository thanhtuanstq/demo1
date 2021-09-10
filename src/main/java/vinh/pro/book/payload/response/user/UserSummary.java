package vinh.pro.book.payload.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSummary
        implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String name;
}
