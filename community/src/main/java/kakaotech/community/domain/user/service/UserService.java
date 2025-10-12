package kakaotech.community.domain.user.service;

import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.UserRepository;
import kakaotech.community.global.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static kakaotech.community.global.exception.code.ExceptionCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}
