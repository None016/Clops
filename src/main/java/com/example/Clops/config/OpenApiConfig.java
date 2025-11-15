package com.example.Clops.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userServiceOpenAPI() {
        final String securitySchemeName = "bearer-key";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Введите JWT токен полученный при аутентификации")))
                .info(new Info()
                        .title("User Service API")
                        .description("""
                            CRUD API для управления пользователями с JWT аутентификацией
                            
                            ## Как использовать:
                            1. Зарегистрируйте пользователя через `/api/auth/register`
                            2. Авторизуйтесь через `/api/auth/login` чтобы получить JWT токен
                            3. Нажмите кнопку **Authorize** и введите токен в формате: `Bearer ваш_токен`
                            4. Теперь вы можете использовать защищенные endpoints
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("support@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
