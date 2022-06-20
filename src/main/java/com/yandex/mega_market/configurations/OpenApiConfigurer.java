package com.yandex.mega_market.configurations;


import com.yandex.mega_market.properties.OpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author D4uranbek
 * @since 20.06.2022
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty( name = "springdoc.swagger-ui.enabled", havingValue = "true", matchIfMissing = true )
public class OpenApiConfigurer {

    private final OpenApiProperties properties;

    @Bean
    public OpenAPI api() {
        return new OpenAPI().info( info() );
    }

    private Info info() {
        return new Info()
                .title( properties.getTitle() )
                .description( properties.getDescription() )
                .version( properties.getVersion() )
                .contact( new Contact()
                        .name( properties.getContactName() )
                        .email( properties.getContactEmail() )
                        .url( properties.getContactUrl() ) )
                .license( new License()
                        .name( properties.getLicenseName() )
                        .url( properties.getLicenseUrl() ) );
    }

}