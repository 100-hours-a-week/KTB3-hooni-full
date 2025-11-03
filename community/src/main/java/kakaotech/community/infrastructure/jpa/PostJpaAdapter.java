package kakaotech.community.infrastructure.jpa;

import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.PostJpaRepository;
import kakaotech.community.domain.post.PostRepository;
import kakaotech.community.domain.post.PostSummaryProjection;
import kakaotech.community.global.page.PageQuery;
import kakaotech.community.global.page.PageResult;
import kakaotech.community.global.page.SortSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
@RequiredArgsConstructor
public class PostJpaAdapter implements PostRepository {
    private final PostJpaRepository postJpaRepository;

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return postJpaRepository.findById(postId);
    }

    @Override
    public PageResult<PostSummaryProjection> findPostsByPaging(PageQuery pageQuery) {
        Sort.Direction direction = (pageQuery.sort().direction() == SortSpec.Direction.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageQuery.pageNum(), pageQuery.pageSize(), Sort.by(direction, pageQuery.sort().property()));

        Page<PostSummaryProjection> pagedPosts = postJpaRepository.findPostSummaries(pageable);

        return new PageResult<>(pagedPosts.toList(), pagedPosts.getNumber(), pagedPosts.getSize(), pagedPosts.getTotalPages(), pagedPosts.getNumberOfElements());
    }

    @Override
    public void deleteById(Long postId) {
        postJpaRepository.deleteById(postId);
    }

    @Override
    public boolean existsById(Long postId) {
        return postJpaRepository.existsById(postId);
    }
}
