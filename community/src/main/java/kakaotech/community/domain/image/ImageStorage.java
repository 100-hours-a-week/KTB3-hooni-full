package kakaotech.community.domain.image;

import kakaotech.community.domain.image.dto.ImageMeta;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageStorage {

    UUID upload(UUID id, MultipartFile file);

    ImageMeta getImage(UUID imageId);
}
