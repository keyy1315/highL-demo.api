package org.example.highlighterdemo.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
///     swagger - api 명세서 비슷한거
///     api 테스트 할 때 용이함 - @Schema 어노테이션 달려있으면 swagger 에서 확인할 수 있음
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(
                new Info()
                        .title("LOL HIGHLIGHTER API")
                        .version("1.0")
                        .description("This is the API description"));

        return openAPI;
    }
}
