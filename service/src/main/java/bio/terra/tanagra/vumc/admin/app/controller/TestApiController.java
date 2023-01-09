package bio.terra.tanagra.vumc.admin.app.controller;

import bio.terra.tanagra.model.SystemVersionV2;
import bio.terra.tanagra.model.UserProfileV2;
import bio.terra.tanagra.vumc.admin.generated.controller.TestApi;
import bio.terra.tanagra.vumc.admin.generated.model.ApiCoreServiceTest;
import bio.terra.tanagra.vumc.admin.service.TanagraCoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class TestApiController implements TestApi {
  private static final Logger LOGGER = LoggerFactory.getLogger(TestApiController.class);

  private final TanagraCoreService tanagraCoreService;

  @Autowired
  public TestApiController(TanagraCoreService tanagraCoreService) {
    this.tanagraCoreService = tanagraCoreService;
  }

  @Override
  public ResponseEntity<ApiCoreServiceTest> coreServiceTest() {
    String version;
    try {
      SystemVersionV2 coreVersion = tanagraCoreService.version();
      version =
          String.format(
              "gitTag: %s, gitHash: %s, github: %s, build: %s",
              coreVersion.getGitTag(),
              coreVersion.getGitHash(),
              coreVersion.getGithub(),
              coreVersion.getBuild());
    } catch (Exception ex) {
      LOGGER.error("core service version", ex);
      version = "error: " + ex.getMessage();
    }

    String authenticatedUser;
    try {
      UserProfileV2 userProfileV2 = tanagraCoreService.currentUser();
      authenticatedUser = "email: " + userProfileV2.getEmail();
    } catch (Exception ex) {
      LOGGER.error("core service authenticated user", ex);
      authenticatedUser = "error: " + ex.getMessage();
    }

    return ResponseEntity.ok(
        new ApiCoreServiceTest().version(version).authenticatedUser(authenticatedUser));
  }
}
