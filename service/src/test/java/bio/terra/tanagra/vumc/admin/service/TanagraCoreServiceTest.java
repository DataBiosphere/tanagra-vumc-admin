package bio.terra.tanagra.vumc.admin.service;

import bio.terra.tanagra.client.ApiException;
import bio.terra.tanagra.vumc.admin.app.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Main.class)
@SpringBootTest
@ActiveProfiles("test")
public class TanagraCoreServiceTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(TanagraCoreServiceTest.class);

  @Autowired private TanagraCoreService tanagraCoreService;

  @Test
  @DisplayName("unauthenticated endpoint")
  void status() throws ApiException {
    tanagraCoreService.status();
    LOGGER.info("status returned success");
  }

  @Test
  @DisplayName("authenticated endpoint")
  void currentUser() throws ApiException {
    String authenticatedCoreUser = tanagraCoreService.currentUser().getEmail();
    Assertions.assertNotNull(authenticatedCoreUser);
    LOGGER.info("authenticated user to core service: {}", authenticatedCoreUser);
  }
}
