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
    private final String accessHeader = "Authorization";
    private final String refreshHeader = "Authorization-refresh";

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(
                new Info()
                        .title("LOL HIGHLIGHTER API")
                        .version("1.0")
                        .description("This is the API description"));

        openAPI.addSecurityItem(new SecurityRequirement().addList(accessHeader));
        openAPI.addSecurityItem(new SecurityRequirement().addList(refreshHeader));
        openAPI.components(createJwtComponents());
        return openAPI;
    }

    private Components createJwtComponents() {
        return new Components()
                .addSecuritySchemes(
                        accessHeader,
                        new SecurityScheme()
                                .name(accessHeader)
                                .description("Format: {access_token}")
                                .type(SecurityScheme.Type.HTTP)
                                .in(SecurityScheme.In.HEADER)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                .addSecuritySchemes(
                        refreshHeader,
                        new SecurityScheme()
                                .name(refreshHeader)
                                .description("Format: Bearer {refresh_token}")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER));
    }
}
