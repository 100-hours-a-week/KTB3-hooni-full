package kakaotech.community.domain.image.dto;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public record ImageMeta(
        Resource resource,
        MediaType mediaType
) {
}
