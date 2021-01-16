package de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rest;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wnck on 04.05.17.
 */

public class BDUserManagementServiceViaRest implements BDUserManagementService {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(BDUserManagementServiceViaRest.class);

  private String baseUrl;
  private final RestTemplate template;

  public BDUserManagementServiceViaRest(final String baseUrl) {
    this.baseUrl = baseUrl;
    template = new RestTemplate();
  }

  private HttpHeaders getHttpHeadersWithUserToken(final Token userToken) {
    // Transport the userToken string in the header
    HttpHeaders headers = new HttpHeaders();
    headers.add("Token", userToken.getToken());
    return headers;
  }

  /**
   * Maps HTTP status codes to application-specific exceptions
   *
   * @param e original rest call exception
   * @throws InvalidTokenException if rest client error exception has status code BAD_REQUEST
   * @throws IllegalParameterException if rest client error exception has status code NOT_FOUND
   * @throws ServiceNotAvailableException if rest client error exception has status code
   * SERVICE_UNAVAILABLE
   */
  private void rethrowException(final HttpClientErrorException e) throws InvalidTokenException,
          IllegalParameterException, ServiceNotAvailableException {
    logger.warn("REST call failed with " + e.getStatusCode());
    HttpStatus statusCode = e.getStatusCode();
    String reason = e.getResponseBodyAsString();
    switch (statusCode) {
      case BAD_REQUEST:
        throw new InvalidTokenException("something went wrong. Reason: " + reason);
      case NOT_FOUND:
        throw new IllegalParameterException("Hm. One parameter is not ok. Reason: " + reason);
      case SERVICE_UNAVAILABLE:
        throw new ServiceNotAvailableException("Remote service not available. Reason: " + reason);
      default:
        throw new ServiceNotAvailableException("Request failed with status code "
                + statusCode + ". Reason: " + reason);
    }
  }



  @Override
  public Token register(final String email, final String password, final String name) throws
          IllegalParameterException, UserNameAlreadyAssignedException,
          ServiceNotAvailableException {
    final String uri = "users";
    Map<String, String> parameters = new HashMap<>();
    parameters.put("email", email);
    parameters.put("secret", password);
    parameters.put("name", name);

    HttpEntity requestEntity = new HttpEntity<Map<String, String>>(parameters);
    try {
      Token token = template.postForObject(baseUrl + uri, requestEntity, Token.class);
      return token;
    } catch (HttpClientErrorException e) {
      logger.warn("REST call failed with " + e.getStatusCode());
      HttpStatus statusCode = e.getStatusCode();
      String reason = e.getResponseBodyAsString();
      switch (statusCode) {
        case NOT_FOUND:
          throw new IllegalParameterException("Hm. One parameter is not ok. Reason: " + reason);
        case SERVICE_UNAVAILABLE:
          throw new ServiceNotAvailableException("Remote service not available. Reason: " + reason);
        default:
          // TODO: Map UserNameAlreadyAssignedException to a HTTP code
          throw new ServiceNotAvailableException("Request failed with status code "
                  + statusCode + ". Reason: " + reason);
      }
    }
  }

  @Override
  public Token login(final String email, final String password) throws IllegalParameterException,
          ServiceNotAvailableException {
    final String uri = "login/{emailString}/";

    HttpEntity<String> entity = new HttpEntity<>(password);
    try {
      Token token = template.postForObject(baseUrl + uri, entity, Token.class, email);
      return token;
    } catch (HttpClientErrorException e) {
      logger.warn("REST call failed with " + e.getStatusCode());
      HttpStatus statusCode = e.getStatusCode();
      String reason = e.getResponseBodyAsString();
      switch (statusCode) {
        case NOT_FOUND:
          throw new IllegalParameterException("Hm. One parameter is not ok. Reason: " + reason);
        case SERVICE_UNAVAILABLE:
          throw new ServiceNotAvailableException("Remote service not available. Reason: " + reason);
        default:
          throw new ServiceNotAvailableException("Request failed with status code "
                  + statusCode + ". Reason: " + reason);
      }
    }
  }

  @Override
  public User resolveUser(final Token userToken) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    final String uri = "users/{tokenString}";

    try {
      User user = template.getForObject(baseUrl + uri, User.class, userToken.getToken());
      return user;
    } catch (HttpClientErrorException e) {
      logger.warn("REST call failed with " + e.getStatusCode());
      HttpStatus statusCode = e.getStatusCode();
      String reason = e.getResponseBodyAsString();
      switch (statusCode) {
        case BAD_REQUEST:
          throw new InvalidTokenException("something went wrong with the token. Reason: " + reason);
        case NOT_FOUND:
          throw new IllegalParameterException("Hm. One parameter is not ok. Reason: " + reason);
        case SERVICE_UNAVAILABLE:
          throw new ServiceNotAvailableException("Remote service not available. Reason: " + reason);
        default:
          throw new ServiceNotAvailableException("Request failed with status code "
                  + statusCode + ". Reason: " + reason);
      }
    }
  }

  @Override
  public List<User> getUsers(final Token userToken) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    final String uri = "users";

    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);



    HttpEntity requestEntity = new HttpEntity<Void>(null, headers);

    // Create an object describing the expected Type in the response
    ParameterizedTypeReference<List<User>> responseType
            = new ParameterizedTypeReference<List<User>>() {
    };

    try {
      ResponseEntity<List<User>> responseEntity = template.exchange(baseUrl + uri, HttpMethod.GET,
              requestEntity, responseType);
      List<User> users = responseEntity.getBody();
      return users;
    } catch (HttpClientErrorException ex) {
      rethrowException(ex);
    }

    // unreachable because of rethrowException ...
    throw new ServiceNotAvailableException(">>>>>>>>> WTF: This should be unreachable code!");
  }

  @Override
  public void changeName(final Token userToken, final String newName) throws
          IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException,
          ServiceNotAvailableException {
    final String uri = "users/{userTokenString}";

    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);

    HttpEntity<String> requestEntity = new HttpEntity<String> (newName, headers);

    try {
      template.put(baseUrl + uri, requestEntity, userToken.getToken());
    } catch (RestClientException e) {
      e.printStackTrace();
    }

  }
}
