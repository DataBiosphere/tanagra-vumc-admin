package bio.terra.tanagra.vumc.admin.app.controller;

import bio.terra.tanagra.vumc.admin.app.configuration.VersionConfiguration;
import bio.terra.tanagra.vumc.admin.generated.controller.UnauthenticatedApi;
import bio.terra.tanagra.vumc.admin.generated.model.ApiSystemVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class UnauthenticatedApiController implements UnauthenticatedApi {
  private static final String GITHUB_COMMIT_URL =
      "https://github.com/DataBiosphere/tanagra-vumc-admin/commit/";
  private final ApiSystemVersion currentVersion;

  @Autowired
  public UnauthenticatedApiController(VersionConfiguration versionConfiguration) {
    this.currentVersion =
        new ApiSystemVersion()
            .gitTag(versionConfiguration.getGitTag())
            .gitHash(versionConfiguration.getGitHash())
            .github(GITHUB_COMMIT_URL + versionConfiguration.getGitHash())
            .build(versionConfiguration.getBuild());
  }

  @Override
  public ResponseEntity<Void> serviceStatus() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApiSystemVersion> serviceVersion() {
    return new ResponseEntity<>(currentVersion, HttpStatus.OK);
  }
}
