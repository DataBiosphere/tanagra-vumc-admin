![Service Build](https://github.com/DataBiosphere/tanagra-vumc-admin/actions/workflows/service-test.yaml/badge.svg?branch=main)
# Tanagra VUMC Admin Service

Tanagra is currently being developed by VUMC and Verily.
The project is shared across the All Of Us and Terra partnerships.

## Development

### Local Postgres
Tests and a local server use a local Postgres database.

To start a postgres container configured with the necessary databases:
```
./service/local-dev/run_postgres.sh start
```
To stop the container:
```
./service/local-dev/run_postgres.sh stop
```
Note that the contents of the database are not saved between container runs.

To connect to the database directly:
```
PGPASSWORD=dbpwd psql postgresql://127.0.0.1:5432/admin_db -U dbuser
```
If you get not found errors running the above command, but the `run_postgres.sh` script calls complete successfully,
check that you don't have PostGres running twice -- e.g. once in Docker and once in a local PostGres installation.

### Build And Run Tests
To get started, build the code and run tests:
```
./gradlew clean build
```

Before a PR can merge, it needs to pass the static analysis checks and tests. To run the checks and tests locally:
```
./gradlew clean check
```

### Local Server
```
./service/local-dev/run_server.sh
```
starts a local server on `localhost:8080`.

See [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for the Swagger API page.

You can also run the local server with authentication disabled. This is useful when testing the UI, which does not
have a login flow yet. (We rely on IAP to handle the Google OAuth flow in our dev deployments.)

```
./service/local-dev/run_server.sh disable-auth
```
