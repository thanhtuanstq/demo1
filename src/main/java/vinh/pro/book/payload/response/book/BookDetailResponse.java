package vinh.pro.book.payload.response.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookDetailResponse
        extends BookResponse {

    private String description;

    private String image;

    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public BookDetailResponse(Long id,
                              String title,
                              String author,
                              String description,
                              String image,
                              LocalDateTime updatedAt,
                              LocalDateTime createdAt) {
        super(id, title, author);

        this.description = description;
        this.image = image;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}
