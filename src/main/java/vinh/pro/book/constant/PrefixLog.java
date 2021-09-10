package vinh.pro.book.constant;

public interface PrefixLog {
    String ERROR = String.format(AppConstant.Common.COMMON_LOG_PREFIX, AppConstant.Code.ERROR_LOG_CODE_PREFIX);
    String AUTH = String.format(AppConstant.Common.COMMON_LOG_PREFIX, AppConstant.Code.AUTH_SERVICE_CODE_PREFIX);
    String USER = String.format(AppConstant.Common.COMMON_LOG_PREFIX, AppConstant.Code.USER_SERVICE_CODE_PREFIX);
    String BOOK = String.format(AppConstant.Common.COMMON_LOG_PREFIX, AppConstant.Code.BOOK_SERVICE_CODE_PREFIX);
}
