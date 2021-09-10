package vinh.pro.book.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import vinh.pro.book.enums.AppErrorType;

@Getter
@ToString
public class BookAppException
        extends RuntimeException {

    private AppErrorType appErrorType;
    private String customMessage = "";
    private String customCode = "";

    public BookAppException() {
    }

    public BookAppException(AppErrorType errorType) {
        super(errorType.getMessage());
        this.appErrorType = errorType;
    }

    public BookAppException(String codeService, String message, HttpStatus status) {
        super(message);
        this.customMessage = message;
        this.customCode = codeService + "_" + status.value();
    }
}
