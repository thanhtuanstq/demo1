package vinh.pro.book.validation;

import org.springframework.http.HttpStatus;
import vinh.pro.book.constant.AppConstant;
import vinh.pro.book.exception.BookAppException;

public class CommonValidation {
    public static void validatePageSize(final int size, final int min, final int max, final String place) {
        if (size < min || size > max) {
            throw new BookAppException(AppConstant.Code.ERROR_LOG_CODE_PREFIX,
                                       String.format("Size of %s must not less than [%d] or granter than [%d]", place, min, max),
                                       HttpStatus.BAD_REQUEST);
        }
    }
}
