package bio.terra.tanagra.vumc.admin.app.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "admin.auth")
public class AuthConfiguration {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthConfiguration.class);

  private boolean disableChecks;
  private boolean iapGkeJwt;
  private boolean iapAppEngineJwt;
  private boolean bearerToken;

  private String gcpProjectNumber;
  private String gcpProjectId;
  private String gkeBackendServiceId;

  public boolean isDisableChecks() {
    return disableChecks;
  }

  public boolean isIapGkeJwt() {
    return iapGkeJwt;
  }

  public boolean isIapAppEngineJwt() {
    return iapAppEngineJwt;
  }

  public boolean isBearerToken() {
    return bearerToken;
  }

  public long getGcpProjectNumber() {
    try {
      return Long.parseLong(gcpProjectNumber);
    } catch (NumberFormatException nfEx) {
      // Don't throw an exception here, which would prevent the service from starting up.
      LOGGER.warn("Invalid GCP project number: {}", gcpProjectNumber);
      return Long.MIN_VALUE;
    }
  }

  public String getGcpProjectId() {
    return gcpProjectId;
  }

  public long getGkeBackendServiceId() {
    try {
      return Long.parseLong(gkeBackendServiceId);
    } catch (NumberFormatException nfEx) {
      // Don't throw an exception here, which would prevent the service from starting up.
      LOGGER.warn("Invalid GCP project number: {}", gkeBackendServiceId);
      return Long.MIN_VALUE;
    }
  }

  public void setDisableChecks(boolean disableChecks) {
    this.disableChecks = disableChecks;
  }

  public void setIapGkeJwt(boolean iapGkeJwt) {
    this.iapGkeJwt = iapGkeJwt;
  }

  public void setIapAppEngineJwt(boolean iapAppEngineJwt) {
    this.iapAppEngineJwt = iapAppEngineJwt;
  }

  public void setBearerToken(boolean bearerToken) {
    this.bearerToken = bearerToken;
  }

  public void setGcpProjectNumber(String gcpProjectNumber) {
    this.gcpProjectNumber = gcpProjectNumber;
  }

  public void setGcpProjectId(String gcpProjectId) {
    this.gcpProjectId = gcpProjectId;
  }

  public void setGkeBackendServiceId(String gkeBackendServiceId) {
    this.gkeBackendServiceId = gkeBackendServiceId;
  }

  /** Write the auth flags into the log. Add an entry here for each new auth flag. */
  public void logConfig() {
    LOGGER.info("Auth config: disable-checks: {}", isDisableChecks());
    LOGGER.info("Auth config: iap-gke-jwt: {}", isIapGkeJwt());
    LOGGER.info("Auth config: iap-appengine-jwt: {}", isIapAppEngineJwt());
    LOGGER.info("Auth config: bearer-token: {}", isBearerToken());
    LOGGER.info("Auth config: gcp-project-number: {}", getGcpProjectNumber());
    LOGGER.info("Auth config: gcp-project-id: {}", getGcpProjectId());
    LOGGER.info("Auth config: gke-backend-service-id: {}", getGkeBackendServiceId());
  }
}
