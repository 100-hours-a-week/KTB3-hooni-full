package kakaotech.community.domain.post;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);

    @Query(value = "SELECT new kakaotech.community.domain.post.PostSummaryProjection(" +
            "p.id, p.title, w.id, w.nickname, w.profileImage," +
            "p.likeCount, p.commentCount, p.viewCount, p.createdAt" +
            ")" +
            "FROM Post p LEFT JOIN p.writer w",
            countQuery = "SELECT COUNT(p) FROM Post p"
    )
    Page<PostSummaryProjection> findPostSummaries(Pageable pageable);
}
