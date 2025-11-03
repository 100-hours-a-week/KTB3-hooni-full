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
import kakaotech.community.domain.post.dto.PostRequest;
import kakaotech.community.domain.post.dto.PostResponse;
import kakaotech.community.global.page.PageResult;
import org.springframework.http.ResponseEntity;

@Tag(name = "게시글 API")
public interface PostApiDocs {

    @Operation(summary = "게시글 생성", description = "제목, 내용, 이미지를 통해 게시글 생성",
            security = {@SecurityRequirement(name = "Bearer Auth")},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data", schema = @Schema(implementation = PostRequest.Create.class)
                    ))
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "게시글 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.Detail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "postId" : 1,
                                                "title" : "제목1",
                                                "content" : "내용1",
                                                "writerId" : 10,
                                                "writerName" : "사용자10",
                                                "writerProfileImage" : (UUID),
                                                "image" : (UUID),
                                                "likeCount" : 0,
                                                "commentCount" : 0,
                                                "viewCount" : 0,
                                                "createdAt" : "2025-10-21T16:37:47.96621",
                                                "isLikedByMe" : false
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "게시글 생성 권한 없음"
            )
    })
    ResponseEntity<PostResponse.Detail> create(Long userId, PostRequest.Create request);

    @Operation(summary = "게시글 조회", description = "20개씩, page를 offset으로 하여 게시글 조회")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.Summaries.class),
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "summaries" : [
                                                {
                                                    "postId" : 1,
                                                "title" : "제목1",
                                                "content" : "내용1",
                                                "writerId" : 10,
                                                "writerName" : "사용자10",
                                                "writerProfileImage" : (UUID),
                                                "image" : (UUID),
                                                "likeCount" : 0,
                                                "commentCount" : 0,
                                                "viewCount" : 0,
                                                "createdAt" : "2025-10-21T16:37:47.96621"
                                                }
                                            ],
                                            "paging" : {
                                                "page" : 0,
                                                "size" : 20,
                                                "totalPages" : 1,
                                                "totalSizes" : 1
                                            }
                                        }
                                        """
                        )
                    )
            )
    })
    ResponseEntity<PageResult<PostResponse.Summary>> getPostsByPaging(@Parameter(required = true) int page);

    @Operation(summary = "게시글 상세 조회")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 상세 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.Detail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "postId" : 1,
                                                "title" : "제목1",
                                                "content" : "내용1",
                                                "writerId" : 10,
                                                "writerName" : "사용자10",
                                                "writerProfileImage" : (UUID),
                                                "image" : (UUID),
                                                "likeCount" : 0,
                                                "commentCount" : 0,
                                                "viewCount" : 0,
                                                "createdAt" : "2025-10-21T16:37:47.96621",
                                                "isLikedByMe" : false
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
    ResponseEntity<PostResponse.Detail> getPost(@Parameter(required = true) Long postId);

    @Operation(summary = "게시글 수정", description = "게시글의 제목과 내용, 이미지를 수정한다",
            security = {@SecurityRequirement(name = "Bearer Auth")},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data", schema = @Schema(implementation = PostRequest.Create.class)
                    ))
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 수정 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.Detail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "postId" : 1,
                                                "title" : "제목1",
                                                "content" : "내용1",
                                                "writerId" : 10,
                                                "writerName" : "사용자10",
                                                "writerProfileImage" : (UUID),
                                                "image" : (UUID),
                                                "likeCount" : 0,
                                                "commentCount" : 0,
                                                "viewCount" : 0,
                                                "createdAt" : "2025-10-21T16:37:47.96621",
                                                "isLikedByMe" : false
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "게시글 수정 권한 없음"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 post id"
            )
    })
    ResponseEntity<PostResponse.Detail> updatePost(@Parameter(hidden = true) Long userId, @Parameter(required = true) Long postId, PostRequest.Update request);

    @Operation(summary = "게시글 삭제")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "게시글 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "게시글 삭제 권한 없음"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 Post id"
            )
    })
    ResponseEntity<?> deletePost(@Parameter(hidden = true) Long userId, @Parameter(required = true) Long postId);
}
