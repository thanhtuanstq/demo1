package vinh.pro.book.payload.response.book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class BookResponse
        implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Long id;

    protected String title;

    protected String author;

    public BookResponse(Long id,
                        String title,
                        String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
}
