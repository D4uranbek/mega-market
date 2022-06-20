package com.yandex.mega_market;

import com.yandex.mega_market.properties.OpenApiProperties;
import com.yandex.mega_market.properties.ServerProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@OpenAPIDefinition
@EnableConfigurationProperties( {
        OpenApiProperties.class,
        ServerProperties.class
} )
@SpringBootApplication
public class MegaMarketApplication {

    public static void main( String[] args ) {
        SpringApplication.run( MegaMarketApplication.class, args );
    }

}
