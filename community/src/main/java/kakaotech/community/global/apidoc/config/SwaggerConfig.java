package kakaotech.community.global.apidoc.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Auth",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "Token"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Swagger API For Community")
                .version("1.0.0");

        return new OpenAPI()
                .info(info);
    }
}

