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
import kakaotech.community.domain.user.dto.UserRequest;
import kakaotech.community.domain.user.dto.UserResponse;
import kakaotech.community.domain.user.port.Token;
import org.springframework.http.ResponseEntity;

@Tag(name = "사용자 API")
public interface UserApiDocs {

    @Operation(summary = "로그인", description = "이메일과 비밀번호를 입력하여 인증을 요청",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json", schema = @Schema(implementation = UserRequest.Login.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "email" : "test@email.com",
                                                "password" : "@ABCabc123"
                                            }
                                            """
                            ))))
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Token.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "accessToken" : "1"
                                            }
                                            """
                            ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "이메일 인증 실패"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "비밀번호 인증 실패"
            )
    })
    ResponseEntity<Token> login(UserRequest.Login request);

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임, 프로필 이미지를 입력하여 회원가입을 요청",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data", schema = @Schema(implementation = UserRequest.Join.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "email" : "test@email.com",
                                                "password" : "@ABCabc1234",
                                                "nickname" : "testNick",
                                                "image" : (binary file)
                                            }
                                            """
                            )
                    )
            ))
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원 가입 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRequest.Join.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id" : 1
                                            }
                                            """
                            ))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 존재하는 이메일 혹은 닉네임"
            )
    })
    ResponseEntity<UserResponse.Join> join(UserRequest.Join request);

    @Operation(summary = "사용자의 프로필 정보 변경", description = "닉네임이나 프로필 이미지를 변경 요청",
            security = {@SecurityRequirement(name = "Bearer Auth")},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data", schema = @Schema(implementation = UserRequest.Update.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "nickname" : "testNick",
                                                "image" : (binary file)
                                            }
                                            """
                            )
                    )
            ))
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "변경 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.Update.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 인자"
            )
    })
    ResponseEntity<UserResponse.Update> changeProfile(@Parameter(hidden = true) Long userId, UserRequest.Update request);

    @Operation(summary = "사용자의 비밀번호 변경", description = "비밀번호 변경",
            security = {@SecurityRequirement(name = "Bearer Auth")},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json", schema = @Schema(implementation = UserRequest.ChangePw.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "password" : "123abcABC@"
                                            }
                                            """
                            )
                    )
            ))
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "변경 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 인자"
            )
    })
    ResponseEntity<?> changePassword(@Parameter(hidden = true) Long userId, UserRequest.ChangePw request);


    @Operation(summary = "사용자 계정 삭제",
        security = {@SecurityRequirement(name = "Bearer Auth")}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "삭제 성공"
            )
    })
    ResponseEntity<?> delete(@Parameter(hidden = true) Long userId);
}
