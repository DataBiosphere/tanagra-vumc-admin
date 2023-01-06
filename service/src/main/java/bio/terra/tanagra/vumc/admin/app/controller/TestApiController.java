package bio.terra.tanagra.vumc.admin.app.controller;

import bio.terra.tanagra.model.UserProfileV2;
import bio.terra.tanagra.vumc.admin.generated.controller.TestApi;
import bio.terra.tanagra.vumc.admin.generated.model.ApiCoreAuthTest;
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

    return ResponseEntity.ok(new ApiCoreAuthTest().noAuth(noAuthMsg).withAuth(withAuthMsg));
  }
}
