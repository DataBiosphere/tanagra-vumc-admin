package bio.terra.tanagra.vumc.admin.app;

import bio.terra.common.migrate.LiquibaseMigrator;
import bio.terra.tanagra.vumc.admin.app.configuration.AdminDatabaseConfiguration;
import bio.terra.tanagra.vumc.admin.app.configuration.AdminDatabaseProperties;
import bio.terra.tanagra.vumc.admin.app.configuration.AuthConfiguration;
import bio.terra.tanagra.vumc.admin.app.configuration.TanagraCoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public final class StartupInitializer {
  private static final Logger LOGGER = LoggerFactory.getLogger(StartupInitializer.class);
  private static final String CHANGELOG_PATH = "db/changelog.xml";

  private StartupInitializer() {}

  public static void initialize(ApplicationContext applicationContext) {
    LOGGER.info("Initializing application before startup");
    // Initialize or upgrade the database depending on the configuration.
    LiquibaseMigrator migrateService = applicationContext.getBean(LiquibaseMigrator.class);
    AdminDatabaseConfiguration tanagraDatabaseConfiguration =
        applicationContext.getBean(AdminDatabaseConfiguration.class);
    AdminDatabaseProperties adminDatabaseProperties =
        applicationContext.getBean(AdminDatabaseProperties.class);
    TanagraCoreConfiguration tanagraCoreConfiguration =
        applicationContext.getBean(TanagraCoreConfiguration.class);
    AuthConfiguration authConfiguration = applicationContext.getBean(AuthConfiguration.class);

    // Log the state of the database migration, tanagra core config, auth flags.
    adminDatabaseProperties.logFlags();
    tanagraCoreConfiguration.logConfig();
    authConfiguration.logConfig();

    // Migrate the database.
    LOGGER.info("Migrating database");
    if (adminDatabaseProperties.isInitializeOnStart()) {
      migrateService.initialize(CHANGELOG_PATH, tanagraDatabaseConfiguration.getDataSource());
    } else if (adminDatabaseProperties.isUpgradeOnStart()) {
      migrateService.upgrade(CHANGELOG_PATH, tanagraDatabaseConfiguration.getDataSource());
    }

    // NOTE:
    // Fill in this method with any other initialization that needs to happen
    // between the point of having the entire application initialized and
    // the point of opening the port to start accepting REST requests.
  }
}
