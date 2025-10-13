package kakaotech.community.domain.common.image;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageStorage {

    UUID upload(UUID id, MultipartFile file);

    Resource getImage(UUID uuid);
}
