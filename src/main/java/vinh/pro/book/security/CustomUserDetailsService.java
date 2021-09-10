package vinh.pro.book.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vinh.pro.book.constant.AppConstant;
import vinh.pro.book.entity.Role;
import vinh.pro.book.entity.User;
import vinh.pro.book.exception.BookAppException;
import vinh.pro.book.repository.RoleRepository;
import vinh.pro.book.repository.UserRepository;

import java.util.Collections;


@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) // username replaced by email
            throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        Role role = roleRepository.findById(user.getRoleId())
                                  .orElseThrow(() -> new BookAppException(
                                          AppConstant.Code.AUTH_SERVICE_CODE_PREFIX,
                                          "Could not found user's role",
                                          HttpStatus.BAD_REQUEST));

        return UserPrincipal.create(user, Collections.singletonList(role.getName().name()));
    }
}
