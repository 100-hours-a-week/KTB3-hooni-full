package kakaotech.community.global.apidoc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kakaotech.community.domain.comment.dto.CommentRequest;
import kakaotech.community.domain.comment.dto.CommentResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "댓글 API")
public interface CommentApiDocs {

    @Operation(summary = "댓글 생성",
    security = {@SecurityRequirement(name = "Bearer Auth")},
    requestBody = @RequestBody(
        required = true,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = CommentRequest.Write.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "content" : "새로운 댓글"
                                    }
                                    """
                    )
            )
    ))
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "댓글 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.Key.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "댓글 작성 권한 없음"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 Post id"
            )
    })
    ResponseEntity<CommentResponse.Key> create(@Parameter(hidden = true) Long userId,
                                               @Parameter(required = true) Long postId,
                                               CommentRequest.Write request);

    @Operation(summary = "댓글 수정",
    security = {@SecurityRequirement(name = "Bearer Auth")},
    requestBody = @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = CommentRequest.Write.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "content" : "수정한 댓글"
                                    }
                                    """
                    )
            )
    ))
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 수정 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.Key.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "댓글 수정 권한 없음"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 post id"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 comment id"
            )
    })
    ResponseEntity<CommentResponse.Key> update(@Parameter(hidden = true) Long userId,
                                               @Parameter(required = true) Long postId,
                                               @Parameter(required = true) Long commentId,
                                               CommentRequest.Write request);

    @Operation(summary = "댓글 삭제",
    security = {@SecurityRequirement(name = "Bearer Auth")})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "댓글 삭제 성공"
            ),
            @ApiResponse(
            responseCode = "403",
            description = "댓글 삭제 권한 없음"
    ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 post id"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 comment id"
            )
    })
    ResponseEntity<?> delete(@Parameter(hidden = true) Long userId,
                             @Parameter(required = true) Long postId,
                             @Parameter(required = true) Long commentId);

    @Operation(summary = "댓글 조회", description = "커서 기반 댓글 조회, cursor 값은 이전 요청의 nextCursor값 혹은 첫 요청이라면 0")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 조회 성공",
                    content = @Content(
                            mediaType = "application/json", schema = @Schema(implementation = CommentResponse.Details.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "postId" : 1,
                                                "details" : [
                                                    {
                                                        "commentId" : 3,
                                                        "writerId" : 5,
                                                        "writerProfileImage" : (UUID),
                                                        "writerName" : "사용자5",
                                                        "content" : "댓글3",
                                                        "createdAt" : "2025-10-21T16:37:47.96621"
                                                    }
                                                ],
                                                "paging" : {
                                                    "nextCursor" : 10,
                                                    "hasNext" : true
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 post id"
            )
    })
    ResponseEntity<?> getComments(@Parameter(required = true) Long postId, @Parameter(required = true) int cursor);
}
