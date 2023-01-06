package bio.terra.tanagra.vumc.admin.app.controller;

import bio.terra.tanagra.vumc.admin.generated.controller.AuthorizationApi;
import bio.terra.tanagra.vumc.admin.generated.model.ApiResourceIdList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AuthorizationApiController implements AuthorizationApi {
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
}
