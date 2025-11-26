package org.example.boardback.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter@Setter
@Component
@ConfigurationProperties(prefix = "app.oauth2")
public class OauthProperties {
    @Value("{app.oauth2.authorized-redirect-uri}")
    private String authorizedRedirectUri;
}
