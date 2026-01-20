package kakaotech.community.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class PostRequest {

    public record Create(
            @NotBlank @Size(max = 26) String title,
            @NotBlank String content,
            MultipartFile image
    ) {
    }

    // Create와 완전히 동일, 그러나 의미적인 인지를 위해 그대로 둠.
    public record Update(
            @NotBlank @Size(max = 26) String title,
            @NotBlank String content,
            MultipartFile image
    ) {
    }
}
