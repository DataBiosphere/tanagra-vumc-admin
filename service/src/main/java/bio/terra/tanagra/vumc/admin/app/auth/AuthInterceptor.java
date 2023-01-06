package bio.terra.tanagra.vumc.admin.app.auth;

import bio.terra.common.exception.InternalServerErrorException;
import bio.terra.common.iam.BearerToken;
import bio.terra.common.iam.BearerTokenFactory;
import bio.terra.tanagra.vumc.admin.app.configuration.AuthConfiguration;
import bio.terra.tanagra.vumc.admin.service.authentication.BearerTokenUtils;
import bio.terra.tanagra.vumc.admin.service.authentication.IapJwtUtils;
import bio.terra.tanagra.vumc.admin.service.authentication.UserId;
import bio.terra.tanagra.vumc.admin.service.authentication.InvalidCredentialsException;
import com.google.api.client.http.HttpMethods;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Service
public class AuthInterceptor implements HandlerInterceptor {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);

  private final AuthConfiguration authConfiguration;

  @Autowired
  public AuthInterceptor(AuthConfiguration authConfiguration) {
    this.authConfiguration = authConfiguration;
  }

  /**
   * Returns true if the request is authenticated and can proceed. Publishes authenticated user info
   * using Spring's SecurityContext.
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // Clear the current user before we start, to make sure we're not using authentication from a
    // previous request.
    SpringAuthentication.clearCurrentUser();

    if (request.getMethod().equals(HttpMethods.OPTIONS)) {
      LOGGER.info("Authorization not required for OPTIONS methods requests");
      return true;
    }

    if (!(handler instanceof HandlerMethod)) {
      LOGGER.error(
          "Unexpected handler class: {}, {}", request.getRequestURL(), request.getMethod());
      return false;
    }

    HandlerMethod method = (HandlerMethod) handler;
    boolean isAuthRequired = false;
    Operation apiOp = AnnotationUtils.findAnnotation(method.getMethod(), Operation.class);
    if (apiOp != null) {
      SecurityRequirement[] authorizations = apiOp.security();
      for (SecurityRequirement auth : apiOp.security()) {
        if (!auth.name().isEmpty()) {
          LOGGER.info("Authorization required by endpoint: {}", request.getRequestURL().toString());
          isAuthRequired = true;
          break;
        }
        LOGGER.info("auth.value(): {}", auth.name());
      }
    } else {
      LOGGER.info(
          "apiOp=null: {}, {}",
          method.getMethod().getName(),
          method.getMethod().getDeclaringClass().getName());
    }
    if (!isAuthRequired) {
      LOGGER.info("Authorization not required by endpoint: {}", request.getRequestURL().toString());
      return true;
    }

    UserId userId;
    try {
      if (authConfiguration.isIapGkeJwt()) {
        String jwt = IapJwtUtils.getJwtFromHeader(request);
        userId =
            IapJwtUtils.verifyJwtForComputeEngineOrGKE(
                jwt,
                authConfiguration.getGcpProjectNumber(),
                authConfiguration.getGkeBackendServiceId());
      } else if (authConfiguration.isIapAppEngineJwt()) {
        String jwt = IapJwtUtils.getJwtFromHeader(request);
        userId =
            IapJwtUtils.verifyJwtForAppEngine(
                jwt, authConfiguration.getGcpProjectNumber(), authConfiguration.getGcpProjectId());
      } else if (authConfiguration.isBearerToken()) {
        BearerToken bearerToken = new BearerTokenFactory().from(request);
        userId = BearerTokenUtils.getUserIdFromToken(bearerToken);
      } else if (authConfiguration.isDisableChecks()) {
        LOGGER.warn(
            "Authentication checks are disabled. This should only happen for local development.");
        userId = UserId.forDisabledAuthentication();
      } else {
        throw new InternalServerErrorException("Invalid auth configuration");
      }
    } catch (InvalidCredentialsException ite) {
      LOGGER.error("Authentication failed", ite);
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }

    SpringAuthentication.setCurrentUser(userId);
    LOGGER.info("User authenticated: subject={}, email={}", userId.getSubject(), userId.getEmail());

    // Any further checks on the user (e.g. check email domain name) should go here.
    // Return SC_FORBIDDEN, not SC_UNAUTHORIZED, if they fail.

    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView) {
    SpringAuthentication.clearCurrentUser();
  }
}
