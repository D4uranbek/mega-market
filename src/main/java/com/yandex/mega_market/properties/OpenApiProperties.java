package com.yandex.mega_market.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author D4uranbek
 * @since 20.06.2022
 */
@Getter
@Setter
@ConfigurationProperties( prefix = "api.info" )
public class OpenApiProperties {

    private String title;

    private String description;

    private String version;

    private String termsOfService;

    private String contactName;

    private String contactEmail;

    private String contactUrl;

    private String licenseName;

    private String licenseUrl;

}
