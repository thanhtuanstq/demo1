package vinh.pro.book.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import vinh.pro.book.constant.AppConstant;
import vinh.pro.book.constant.PrefixLog;
import vinh.pro.book.payload.response.auth.ApiResponse;
import vinh.pro.book.utils.DateTimeUtils;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalHandleException {

    private ApiResponse buildAppResponseInternal(BookAppException exception) {
        log.error(PrefixLog.ERROR, exception);

        return ApiResponse.builder()
                          .success(false)
                          .code(!"".equals(exception.getCustomCode()) ? exception.getCustomCode() : exception
                                  .getAppErrorType().getCode())
                          .message(!"".equals(exception.getCustomMessage()) ? exception.getCustomMessage() : exception
                                  .getAppErrorType().getMessage())
                          .timestamp(DateTimeUtils.getISOLocalDateTime())
                          .build();
    }

    private ApiResponse buildAppResponseCommon(Exception exception, HttpStatus status) {
        log.error(PrefixLog.ERROR, exception);

        return ApiResponse.builder()
                          .success(false)
                          .code(AppConstant.Code.ERROR_LOG_CODE_PREFIX + "_" + status.value())
                          .message(exception.getLocalizedMessage())
                          .timestamp(DateTimeUtils.getISOLocalDateTime())
                          .build();
    }

    @ExceptionHandler(BookAppException.class)
    @ResponseBody
    public ApiResponse handleShopAppException(BookAppException ex) {
        return buildAppResponseInternal(ex);
    }


    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class,
            AccessDeniedException.class,
            InsufficientAuthenticationException.class,
            AuthenticationException.class})
    @ResponseBody
    public ApiResponse handleException(Exception ex) {
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            return buildAppResponseCommon(ex, HttpStatus.METHOD_NOT_ALLOWED);
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            return buildAppResponseCommon(ex, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            return buildAppResponseCommon(ex, HttpStatus.NOT_ACCEPTABLE);
        } else if (ex instanceof MissingPathVariableException) {
            return buildAppResponseCommon(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof MissingServletRequestParameterException) {
            return buildAppResponseCommon(ex, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ServletRequestBindingException) {
            return buildAppResponseCommon(ex, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ConversionNotSupportedException) {
            return buildAppResponseCommon(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof TypeMismatchException) {
            return buildAppResponseCommon(ex, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof HttpMessageNotReadableException) {
            return buildAppResponseCommon(ex, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof HttpMessageNotWritableException) {
            return buildAppResponseCommon(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof MethodArgumentNotValidException) {
            return buildAppResponseCommon(ex, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof MissingServletRequestPartException) {
            return buildAppResponseCommon(ex, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof BindException) {
            return buildAppResponseCommon(ex, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof NoHandlerFoundException) {
            return buildAppResponseCommon(ex, HttpStatus.NOT_FOUND);
        } else if (ex instanceof AsyncRequestTimeoutException) {
            return buildAppResponseCommon(ex, HttpStatus.SERVICE_UNAVAILABLE);
        } else if (ex instanceof AccessDeniedException) {
            return buildAppResponseCommon(ex, HttpStatus.FORBIDDEN);
        } else if (ex instanceof InsufficientAuthenticationException) {
            return buildAppResponseCommon(ex, HttpStatus.UNAUTHORIZED);
        } else if (ex instanceof AuthenticationException) {
            return buildAppResponseCommon(ex, HttpStatus.UNAUTHORIZED);
        } else {
            return buildAppResponseCommon(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
