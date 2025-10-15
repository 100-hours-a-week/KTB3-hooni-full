package kakaotech.community.domain.postlike.service;

import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.PostRepository;
import kakaotech.community.domain.postlike.PostLike;
import kakaotech.community.domain.postlike.PostLikeRepository;
import kakaotech.community.global.exception.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static kakaotech.community.global.exception.code.ExceptionCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final Lock lock = new ReentrantLock();

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public void like(Long postId, Long userId) {
        lock.lock();
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new PostException(POST_NOT_FOUND));

            post.liked();
            postRepository.save(post);

            postLikeRepository.save(new PostLike(postId, userId));
        } finally {
            lock.unlock();
        }
    }

    public void unlike(Long postId, Long userId) {
        lock.lock();
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new PostException(POST_NOT_FOUND));

            post.unliked();
            postRepository.save(post);

            postLikeRepository.delete(postId, userId);
        } finally {
            lock.unlock();
        }
    }

}
