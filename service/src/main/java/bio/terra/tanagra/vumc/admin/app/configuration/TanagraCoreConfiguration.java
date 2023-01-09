package bio.terra.tanagra.vumc.admin.app.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "admin.tanagra-core")
public class TanagraCoreConfiguration {
  private static final Logger LOGGER = LoggerFactory.getLogger(TanagraCoreConfiguration.class);

  private String basePath;
  private String oauthClientId;
  private boolean useAdc;

  public String getBasePath() {
    return basePath;
  }

  public String getOauthClientId() {
    return oauthClientId;
  }

  public boolean isUseAdc() {
    return useAdc;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public void setOauthClientId(String oauthClientId) {
    this.oauthClientId = oauthClientId;
  }

  public void setUseAdc(boolean useAdc) {
    this.useAdc = useAdc;
  }

  /** Write the config properties into the log. Add an entry here for each new config property. */
  public void logConfig() {
    LOGGER.info("Tanagra Core: base-path: {}", getBasePath());
    LOGGER.info("Tanagra Core: oauth-client-id: {}", getOauthClientId());
    LOGGER.info("Tanagra Core: use-adc: {}", isUseAdc());
  }
}
