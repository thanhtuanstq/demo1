package vinh.pro.book.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vinh.pro.book.constant.AppConstant;
import vinh.pro.book.constant.PrefixLog;
import vinh.pro.book.exception.BookAppException;
import vinh.pro.book.payload.request.auth.LoginRequest;
import vinh.pro.book.security.JwtTokenProvider;
import vinh.pro.book.utils.CommonUtils;

@Service
@Slf4j
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public String login(LoginRequest loginRequest) {
        log.info(PrefixLog.AUTH + "login user by email = [{}] is starting...", loginRequest.getEmail());
        if (!CommonUtils.isValidMail(loginRequest.getEmail())) {
            throw new BookAppException(AppConstant.Code.AUTH_SERVICE_CODE_PREFIX,
                                       "Email is invalid!",
                                       HttpStatus.BAD_REQUEST);
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            throw new BookAppException(AppConstant.Code.AUTH_SERVICE_CODE_PREFIX,
                                       "Email or password is incorrect!",
                                       HttpStatus.BAD_REQUEST);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        log.info(PrefixLog.AUTH + "login user by email = [{}] finished", loginRequest.getEmail());

        return jwt;
    }
}
