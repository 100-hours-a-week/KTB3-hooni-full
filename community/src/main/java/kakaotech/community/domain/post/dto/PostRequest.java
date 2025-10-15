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
}
