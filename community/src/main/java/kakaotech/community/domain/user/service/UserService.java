package kakaotech.community.domain.user.service;

import kakaotech.community.domain.image.service.ImageService;
import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.UserRepository;
import kakaotech.community.domain.user.dto.UserResponse;
import kakaotech.community.global.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static kakaotech.community.global.exception.code.ExceptionCode.DUPLICATED_EMAIL_OR_NICKNAME;
import static kakaotech.community.global.exception.code.ExceptionCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ImageService imageService;

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    public UserResponse.Join join(String email, String password, String nickname, MultipartFile image) {
        if (isDuplicatedEmail(email) | isDuplicatedNickname(nickname)) {
            throw new UserException(DUPLICATED_EMAIL_OR_NICKNAME);
        }

        UUID imageId = imageService.save(image);

        Long userId = userRepository.save(new User(email, password, nickname, imageId));
        return new UserResponse.Join(userId);
    }

    private boolean isDuplicatedEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isDuplicatedNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public UserResponse.Update updateProfile(Long userId, String nickname, MultipartFile image) {
        User user = findById(userId);

        if (nickname != null) {
            changeNickname(user, nickname);
        }

        if (image != null) {
            changeProfileImage(user, image);
        }

        return new UserResponse.Update(user.getId(), user.getEmail(), user.getNickname(), user.getProfileImageId());
    }

    private void changeNickname(User user, String nickname) {
        user.updateNickname(nickname);
        userRepository.save(user);
    }

    private void changeProfileImage(User user, MultipartFile image) {
        UUID uuid = imageService.updateImage(user.getProfileImageId(), image);
        user.updateProfileImage(uuid);
        userRepository.save(user);
    }
}
