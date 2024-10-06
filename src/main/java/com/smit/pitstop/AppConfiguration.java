package com.smit.pitstop;

import com.smit.pitstop.service.connector.IApiConnector;
import com.smit.pitstop.service.connector.JsonApiConnector;
import com.smit.pitstop.service.connector.XmlApiConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public IApiConnector xmlApiConnector() {
        return new XmlApiConnector();
    }

    @Bean
    public IApiConnector jsonApiConnector() {
        return new JsonApiConnector();
    }
}
