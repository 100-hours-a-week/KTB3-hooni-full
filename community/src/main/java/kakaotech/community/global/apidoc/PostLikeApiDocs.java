package kakaotech.community.global.apidoc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "게시글 좋아요 API")
public interface PostLikeApiDocs {

    @Operation(summary = "게시글 좋아요")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요 성공",
                    content = @Content(mediaType = "applicaton/json",
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "result" : "success"
                                    }
                                    """
                    ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 post id"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "좋아요 누를 권한 없음"
            )
    })
    ResponseEntity<?> likePost(@Parameter(hidden = true) Long userId, @Parameter(required = true) Long postId);

    @Operation(summary = "게시글 좋아요 취소")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요 취소 성공",
                    content = @Content(mediaType = "applicaton/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "result" : "success"
                                    }
                                    """
                            ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 post id"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "좋아요 취소 누를 권한 없음"
            )
    })
    ResponseEntity<?> unlikePost(@Parameter(hidden = true) Long userId, @Parameter(required = true) Long postId);
}
