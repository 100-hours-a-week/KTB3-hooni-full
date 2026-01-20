package kakaotech.community.domain.post;

import kakaotech.community.global.page.PageQuery;
import kakaotech.community.global.page.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long postId);

    PageResult<PostSummaryProjection> findPostsByPaging(PageQuery pageQuery);

    void deleteById(Long postId);

    boolean existsById(Long postId);
}
