package vinh.pro.book.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vinh.pro.book.constant.AppConstant;
import vinh.pro.book.payload.request.auth.LoginRequest;
import vinh.pro.book.payload.response.auth.LoginResponse;
import vinh.pro.book.payload.response.auth.ApiResponse;
import vinh.pro.book.service.AuthService;


@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestBody LoginRequest loginRequest) {

        return new ApiResponse(
                AppConstant.Code.AUTH_SERVICE_CODE_PREFIX,
                HttpStatus.OK,
                "Login success",
                new LoginResponse(authService.login(loginRequest)));
    }
}
