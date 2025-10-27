package kakaotech.community.domain.post.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class PostRequest {

    public record Create(
            @NotNull @Size(max = 26) String title,
            @NotNull String content,
            MultipartFile image
    ) {
    }

    // Create와 완전히 동일, 그러나 의미적인 인지를 위해 그대로 둠.
    public record Update(
            @NotNull @Size(max = 26) String title,
            @NotNull String content,
            MultipartFile image
    ) {
    }
}
