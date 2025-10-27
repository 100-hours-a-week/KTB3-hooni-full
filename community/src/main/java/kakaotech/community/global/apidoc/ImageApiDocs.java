package kakaotech.community.global.apidoc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "이미지 API")
public interface ImageApiDocs {

    @Operation(summary = "이미지 조회", description = "이미지 id를 요청하면 실제 이미지인 바이너리를 찾아서 조회. 응답의 Content-Type은 저장된 원본 이미지의 미디어 타입(png/jpeg/gif/webp 등)으로 설정")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "이미지 조회 성공 - 바이너리",
                    content = {
                            @Content(mediaType = "image/png", schema = @Schema(type = "string", format = "binary"))
                    },
                    headers = {
                            @Header(name = "Content-Type", description = "실제 이미지의 미디어 타입 (예: image/png, image/jpeg)"),
                            @Header(name = "Content-Length", description = "응답 바이트 길이", schema = @Schema(type = "integer", format = "int64"))
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 UUID"
            )
    })
    ResponseEntity<Resource> get(@Parameter(required = true) UUID id);
}
