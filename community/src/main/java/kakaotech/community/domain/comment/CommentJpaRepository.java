package kakaotech.community.domain.comment;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface CommentJpaRepository extends JpaRepository<Comment, Long>, CommentRepository {
}
