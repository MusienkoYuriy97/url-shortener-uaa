package by.solbegsoft.urlshorteneruaa.config;

import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;

@Configuration
public class SwaggerConfig {
    private static final String API_KEY = "ApiKey";

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(API_KEY,apiKeySecuritySchema()))
                .info(
                        new Info()
                                .title("url-shortener-uaa documentation")
                                .version("1.0")
                                .contact(
                                        new Contact()
                                                .email("97musienko@gmail.com")
                                                .name("Yury Musienko")
                                )
                )
                .security(Collections.singletonList(new SecurityRequirement().addList(API_KEY)));
    }

    @Bean
    public OpenApiCustomiser schemaCustomiser() {
        ResolvedSchema resolvedSchemaUser = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(User.class));
        ResolvedSchema resolvedSchemaActivateKey = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(ActivateKey.class));

        return openApi -> openApi
                .schema(resolvedSchemaUser.schema.getName(), resolvedSchemaUser.schema)
                .schema(resolvedSchemaActivateKey.schema.getName(), resolvedSchemaActivateKey.schema);
    }

    public SecurityScheme apiKeySecuritySchema() {
        return new SecurityScheme()
                .name("Authorization")
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.APIKEY);
    }
}