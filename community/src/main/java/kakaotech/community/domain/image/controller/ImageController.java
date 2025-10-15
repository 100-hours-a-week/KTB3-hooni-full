package kakaotech.community.domain.image.controller;

import kakaotech.community.domain.image.dto.ImageMeta;
import kakaotech.community.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> get(@PathVariable UUID id) {
        ImageMeta imageMeta = imageService.load(id);

        return ResponseEntity.ok()
                .contentType(imageMeta.mediaType())
                .body(imageMeta.resource());
    }
}
