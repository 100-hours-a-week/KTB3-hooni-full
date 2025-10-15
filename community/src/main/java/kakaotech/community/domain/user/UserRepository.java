package kakaotech.community.domain.user;

import java.util.Optional;

public interface UserRepository {

    Long save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
