package bio.terra.tanagra.vumc.admin.app.controller;

import bio.terra.tanagra.model.UserProfileV2;
import bio.terra.tanagra.vumc.admin.generated.controller.AuthorizationApi;
import bio.terra.tanagra.vumc.admin.generated.model.ApiCoreAuthTest;
import bio.terra.tanagra.vumc.admin.generated.model.ApiResourceIdList;
import bio.terra.tanagra.vumc.admin.service.TanagraCoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AuthorizationApiController implements AuthorizationApi {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationApiController.class);

  private final TanagraCoreService tanagraCoreService;

  @Autowired
  public AuthorizationApiController(TanagraCoreService tanagraCoreService) {
    this.tanagraCoreService = tanagraCoreService;
  }

  @SuppressWarnings("PMD.UseObjectForClearerAPI")
  @Override
  public ResponseEntity<Void> isAuthorized(
      String action, String resourceType, String resourceId, String userEmail) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<ApiResourceIdList> listAuthorizedResources(
      String resourceType, String userEmail) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<ApiCoreAuthTest> coreAuthTest() {
    String noAuthMsg;
    try {
      tanagraCoreService.status();
      noAuthMsg = "status=success";
    } catch (Exception ex) {
      LOGGER.error("no auth", ex);
      noAuthMsg = "status=error: " + ex.getMessage();
    }

    String withAuthMsg;
    try {
      UserProfileV2 userProfileV2 = tanagraCoreService.currentUser();
      withAuthMsg = "getMe=" + userProfileV2.getEmail();
    } catch (Exception ex) {
      LOGGER.error("with auth", ex);
      withAuthMsg = "getMe=error: " + ex.getMessage();
    }

    return ResponseEntity.ok(
        new ApiCoreAuthTest()
            .noAuth("no-auth: " + noAuthMsg)
            .withAuth("with-auth: " + withAuthMsg));
  }
}
