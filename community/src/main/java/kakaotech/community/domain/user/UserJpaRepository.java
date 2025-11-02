package kakaotech.community.domain.user;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Primary
public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
