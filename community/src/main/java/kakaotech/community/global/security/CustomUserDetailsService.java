package kakaotech.community.global.security;

import kakaotech.community.domain.user.UserRepository;
import kakaotech.community.global.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static kakaotech.community.global.exception.code.ExceptionCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findById(Long.parseLong(userId))
                .map(this::toDetails)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private UserDetails toDetails(kakaotech.community.domain.user.User user) {
        return new User(
                String.valueOf(user.getId()),
                "",
                Collections.emptyList()
        );
    }
}
