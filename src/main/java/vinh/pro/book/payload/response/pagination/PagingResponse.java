package vinh.pro.book.payload.response.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponse
        implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long total;

    @JsonProperty("page_no")
    private Integer pageNo;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("total_page")
    private Integer totalPage;

    private List<?> records;

    public Integer getTotalPage() {
        return (total % pageSize == 0) ? (int) (total / pageSize) : (int) (total / pageSize + 1);
    }
}
