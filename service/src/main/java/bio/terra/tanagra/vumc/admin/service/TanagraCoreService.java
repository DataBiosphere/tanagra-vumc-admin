package bio.terra.tanagra.vumc.admin.service;

import bio.terra.common.logging.RequestIdFilter;
import bio.terra.tanagra.api.UnauthenticatedApi;
import bio.terra.tanagra.api.UsersV2Api;
import bio.terra.tanagra.client.ApiClient;
import bio.terra.tanagra.client.ApiException;
import bio.terra.tanagra.model.SystemVersionV2;
import bio.terra.tanagra.model.UserProfileV2;
import bio.terra.tanagra.vumc.admin.app.auth.SpringAuthentication;
import bio.terra.tanagra.vumc.admin.app.configuration.TanagraCoreConfiguration;
import bio.terra.tanagra.vumc.admin.service.authentication.AppDefaultUtils;
import bio.terra.tanagra.vumc.admin.service.authentication.UserId;
import javax.ws.rs.client.Client;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TanagraCoreService {
  private final TanagraCoreConfiguration tanagraCoreConfiguration;
  private final Client commonHttpClient;

  @Autowired
  public TanagraCoreService(TanagraCoreConfiguration tanagraCoreConfiguration) {
    this.tanagraCoreConfiguration = tanagraCoreConfiguration;
    this.commonHttpClient = new ApiClient().getHttpClient();
  }

  private ApiClient getApiClient(String accessToken) {
    ApiClient client =
        new ApiClient()
            .setBasePath(tanagraCoreConfiguration.getBasePath())
            .setHttpClient(commonHttpClient)
            .addDefaultHeader(
                RequestIdFilter.REQUEST_ID_HEADER, MDC.get(RequestIdFilter.REQUEST_ID_MDC_KEY));
    client.setAccessToken(accessToken);
    return client;
  }

  /**
   * Return an ApiClient with a token from the currently authenticated user or the application
   * default credentials, depending on the configuration flag.
   */
  private ApiClient getApiClientAuthenticated() {
    UserId userId =
        tanagraCoreConfiguration.isUseAdc()
            ? AppDefaultUtils.getUserIdFromAdc(tanagraCoreConfiguration.getOauthClientId())
            : SpringAuthentication.getCurrentUser();
    return getApiClient(userId.getToken());
  }

  public SystemVersionV2 version() throws ApiException {
    // Use an authenticated client here, even though the version endpoint is part of the
    // UnauthenticatedApi in case all endpoints are behind IAP.
    return new UnauthenticatedApi(getApiClientAuthenticated()).serviceVersion();
  }

  public UserProfileV2 currentUser() throws ApiException {
    return new UsersV2Api(getApiClientAuthenticated()).getMe();
  }
}
