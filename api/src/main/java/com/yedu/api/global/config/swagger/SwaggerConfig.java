package com.yedu.api.global.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
  @Bean
  public GroupedOpenApi chatOpenApi() {
    String[] paths = {"/**"};

    return GroupedOpenApi.builder().group("Y-EDU API v1").pathsToMatch(paths).build();
  }

  @Bean
  public OpenAPI openApi() {
    String jwt = "JWT";
    SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT");
    Components components =
        new Components()
            .addSecuritySchemes(
                jwt,
                new SecurityScheme()
                    .name(jwt)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"));

    SpringDocUtils.getConfig()
        .replaceWithSchema(
            LocalTime.class,
            new StringSchema()
                .example(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))));

    return new OpenAPI()
        .addServersItem(new Server().url("/"))
        .addSecurityItem(securityRequirement)
        .components(components);
  }
}
