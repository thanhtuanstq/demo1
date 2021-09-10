package vinh.pro.book.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vinh.pro.book.constant.AppConstant;
import vinh.pro.book.constant.PrefixLog;
import vinh.pro.book.payload.request.auth.SignUpRequest;
import vinh.pro.book.payload.response.auth.ApiResponse;
import vinh.pro.book.payload.response.user.UserSummary;
import vinh.pro.book.security.CurrentUser;
import vinh.pro.book.security.UserPrincipal;
import vinh.pro.book.service.UserService;


@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @ResponseBody
    public Object register(@RequestBody SignUpRequest signUpRequest) {

        userService.create(signUpRequest);

        return new ApiResponse(
                AppConstant.Code.USER_SERVICE_CODE_PREFIX,
                HttpStatus.CREATED,
                "User registered successfully",
                null);
    }

    @GetMapping("/me")
    @ResponseBody
    @PreAuthorize("hasAnyRole({'USER', 'ADMIN'})")
    public Object me(@CurrentUser UserPrincipal userPrincipal) {

        UserSummary userSummary = userService.getCurrentUserSummaryInfo(userPrincipal);
        log.info(PrefixLog.USER + "get summary of current user finished");

        return new ApiResponse(
                AppConstant.Code.USER_SERVICE_CODE_PREFIX,
                HttpStatus.OK,
                "Summary info of current user",
                userSummary);
    }
}
