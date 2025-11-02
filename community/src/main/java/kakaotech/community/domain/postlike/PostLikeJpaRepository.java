package kakaotech.community.domain.postlike;

import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.user.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Primary
public interface PostLikeJpaRepository extends JpaRepository<PostLike, Long>, PostLikeRepository {

    @Modifying
    @Query("DELETE FROM PostLike pl WHERE pl.post = :post AND pl.user = :user")
    void delete(Post post, User user);

    boolean existsByPostAndUser(Post post, User user);
}
