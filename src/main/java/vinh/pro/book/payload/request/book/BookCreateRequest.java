package vinh.pro.book.payload.request.book;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class BookCreateRequest
        implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String author;
    private String description;
    private String image;
}
