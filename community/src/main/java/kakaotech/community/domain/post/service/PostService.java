package kakaotech.community.domain.post.service;

import kakaotech.community.domain.image.service.ImageService;
import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.PostRepository;
import kakaotech.community.domain.post.PostSummaryProjection;
import kakaotech.community.domain.post.dto.PostResponse;
import kakaotech.community.domain.postlike.service.PostLikeQueryService;
import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.service.UserService;
import kakaotech.community.global.exception.PostException;
import kakaotech.community.global.page.PageQuery;
import kakaotech.community.global.page.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static kakaotech.community.global.exception.code.ExceptionCode.POST_NOT_FOUND;
import static kakaotech.community.global.exception.code.ExceptionCode.POST_WRITER_MISMATCH;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final UserService userService;
    private final ImageService imageService;
    private final PostLikeQueryService postLikeQueryService;

    private final PostRepository postRepository;

    public PostResponse.Detail create(Long userId, String title, String content, MultipartFile image) {
        User user = userService.findById(userId);
        UUID imageId = imageService.save(image);

        Post post = postRepository.save(new Post(user, title, content, imageId));

        return toDetail(user, post);
    }

    public PostResponse.Detail getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        User writer = userService.findById(post.getWriterId());

        post.viewedOne();

        return toDetail(writer, post);
    }

    @Transactional(readOnly = true)
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public PageResult<PostResponse.Summary> getPostsByPaging(int page) {
        PageResult<PostSummaryProjection> postSummaries = postRepository.findPostsByPaging(PageQuery.offset(page));

        return new PageResult<>(toSummariesResponse(postSummaries.elements()),
                postSummaries.pageNum(), postSummaries.pageSize(), postSummaries.totalPage(), postSummaries.totalSize());
    }

    private List<PostResponse.Summary> toSummariesResponse(List<PostSummaryProjection> projections) {
        return projections.stream()
                .map(p -> new PostResponse.Summary(
                        p.postId(), p.title(), p.writerId(),
                        p.writerName(), p.writerProfileImage(), p.likeCount(),
                        p.commentCount(), p.viewCount(), p.createdAt()
                )).toList();
    }

    private PostResponse.Detail toDetail(User user, Post post) {
        boolean liked = postLikeQueryService.isLiked(post, user);

        return PostMapper.toDetail(user, post, liked);
    }

    public PostResponse.Detail update(Long userId, Long postId, String title, String content, MultipartFile image) {
        Post prePost = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        if (!prePost.isWrittenBy(userId)) {
            throw new PostException(POST_WRITER_MISMATCH);
        }

        UUID newImageId = imageService.updateImage(prePost.getImageId(), image);

        Post newPost = postRepository.save(prePost.update(title, content, newImageId));
        User user = userService.findById(userId);

        return toDetail(user, newPost);
    }

    public void delete(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        if (!post.isWrittenBy(userId)) {
            throw new PostException(POST_WRITER_MISMATCH);
        }

        imageService.delete(post.getImageId());
        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public void validatePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostException(POST_NOT_FOUND);
        }
    }
}
