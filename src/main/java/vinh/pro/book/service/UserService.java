package vinh.pro.book.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vinh.pro.book.constant.AppConstant;
import vinh.pro.book.constant.PrefixLog;
import vinh.pro.book.entity.Role;
import vinh.pro.book.entity.User;
import vinh.pro.book.enums.RoleNameType;
import vinh.pro.book.exception.BookAppException;
import vinh.pro.book.payload.request.auth.SignUpRequest;
import vinh.pro.book.payload.response.user.UserSummary;
import vinh.pro.book.repository.RoleRepository;
import vinh.pro.book.repository.UserRepository;
import vinh.pro.book.security.UserPrincipal;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void create(SignUpRequest signUpRequest) {
        log.info(PrefixLog.USER + "register user starting...");

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BookAppException(
                    AppConstant.Code.AUTH_SERVICE_CODE_PREFIX,
                    "Email Address already in used!",
                    HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                        .email(signUpRequest.getEmail())
                        .password(signUpRequest.getPassword())
                        .firstName(signUpRequest.getFirstName())
                        .lastName(signUpRequest.getLastName())
                        .avatar(signUpRequest.getAvatar())
                        .enabled(true)
                        .build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName(RoleNameType.ROLE_USER)
                                  .orElseThrow(() -> new BookAppException(
                                          AppConstant.Code.AUTH_SERVICE_CODE_PREFIX,
                                          "User Role not set.",
                                          HttpStatus.BAD_REQUEST));
        user.setRoleId(role.getId());
        userRepository.save(user);

        log.info(PrefixLog.USER + "register user finish");
    }

    public UserSummary getCurrentUserSummaryInfo(UserPrincipal userPrincipal) {
        log.info(PrefixLog.USER + "get summary of current user starting...");

        return UserSummary.builder()
                          .id(userPrincipal.getId())
                          .name(userPrincipal.getUsername())
                          .email(userPrincipal.getEmail())
                          .build();
    }
}
