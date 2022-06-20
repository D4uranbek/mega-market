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
@ConfigurationProperties( prefix = "service.prop" )
public class ServerProperties {

    private String port;

    private String ip;

    private String url;

    private String protocol;

    public String getServerUrl() {
        return this.protocol + "://" + this.ip + ":" + this.port;
    }
}
