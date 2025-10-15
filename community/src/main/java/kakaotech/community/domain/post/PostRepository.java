package kakaotech.community.domain.post;

import java.util.List;

public interface PostRepository {

    Post save(Post post);

    List<Post> findPostsByPaging(int page);

    int size();
}
