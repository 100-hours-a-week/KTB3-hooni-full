package kakaotech.community.global.auth.resolver;

import kakaotech.community.global.auth.annotation.Authenticated;
import kakaotech.community.global.exception.AuthException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static kakaotech.community.global.exception.code.ExceptionCode.AUTH_TOKEN_NOT_FOUND;
import static kakaotech.community.global.exception.code.ExceptionCode.INVALID_AUTH_TOKEN;

@Component
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class) && parameter.hasParameterAnnotation(Authenticated.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authHeader = webRequest.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new AuthException(AUTH_TOKEN_NOT_FOUND);
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        return parseToken(token);
    }

    private Long parseToken(String token) {
        try {
            return Long.parseLong(token);
        } catch (NumberFormatException e) {
            throw new AuthException(INVALID_AUTH_TOKEN);
        }
    }
}
