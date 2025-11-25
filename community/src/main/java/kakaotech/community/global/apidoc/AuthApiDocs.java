package kakaotech.community.global.apidoc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kakaotech.community.domain.user.dto.UserRequest;
import kakaotech.community.domain.user.port.Token;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증 API")
public interface AuthApiDocs {

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
}
