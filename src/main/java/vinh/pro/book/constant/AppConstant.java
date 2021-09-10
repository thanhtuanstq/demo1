package vinh.pro.book.constant;

public interface AppConstant {

    interface Common {
        // common logger prefix
        String COMMON_LOG_PREFIX = "BOOK APP LOG -----> %s SERVICE: "; // replace with service name

        // page size of book list
        Integer BOOK_LIST_SIZE_MAX = 100;
        Integer BOOK_LIST_SIZE_MIN = 0;
    }

    interface Code {
        // ============== COMMON SERVICE CODE PREFIX ==============
        // error log service
        String ERROR_LOG_CODE_PREFIX = "ERROR";

        // authenticate service
        String AUTH_SERVICE_CODE_PREFIX = "AUTH";

        // authenticate service
        String USER_SERVICE_CODE_PREFIX = "USER";

        // book service
        String BOOK_SERVICE_CODE_PREFIX = "BOOK";
    }
}
