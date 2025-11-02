package kakaotech.community.domain.post;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface PostJpaRepository extends JpaRepository<Post, Long>, PostRepository {
}
