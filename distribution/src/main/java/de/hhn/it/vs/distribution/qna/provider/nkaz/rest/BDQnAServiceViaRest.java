package de.hhn.it.vs.distribution.qna.provider.nkaz.rest;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BDQnAServiceViaRest implements BDQnAService {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(BDQnAService.class);
  private final String baseUrl;
  private final RestTemplate template;

  public BDQnAServiceViaRest(final String baseUrl) {
    this.baseUrl = baseUrl;
    this.template = new RestTemplate();
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
   *     SERVICE_UNAVAILABLE
   */
  private void rethrowException(final HttpClientErrorException e)
      throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException {
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
        throw new ServiceNotAvailableException(
            "Request failed with status code " + statusCode + ". Reason: " + reason);
    }
  }

  @Override
  public long createArea(final Token userToken, final Area area)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Area> requestEntity = new HttpEntity<>(area, headers);
    try {
      return template.postForObject(baseUrl + URI, requestEntity, long.class);
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    throw new ServiceNotAvailableException(">>>>>>>>> WTF: This should be unreachable code!");
  }

  @Override
  public long createQuestion(final Token userToken, final long areaId, final Question question)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas/{areaId}/questions/";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Question> requestEntity = new HttpEntity<Question>(question, headers);
    try {
      return template.postForObject(baseUrl + URI, requestEntity, long.class, areaId);
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    throw new ServiceNotAvailableException(">>>>>>>>> WTF: This should be unreachable code!");
  }

  @Override
  public long createAnswer(Token userToken, long areaId, long questionId, Answer answer)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas/{areaId}/questions/{questionId}/answers";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Answer> requestEntity = new HttpEntity<>(answer, headers);

    try {
      return template.postForObject(baseUrl + URI, requestEntity, long.class, areaId, questionId);
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    throw new ServiceNotAvailableException(">>>>>>>>> WTF: This should be unreachable code!");
  }

  @Override
  public List<Long> getAreaIds(Token userToken)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
    ParameterizedTypeReference<List<Long>> responseType =
        new ParameterizedTypeReference<List<Long>>() {};

    try {
      ResponseEntity<List<Long>> responseEntity =
          template.exchange(baseUrl + URI, HttpMethod.GET, requestEntity, responseType);
      return responseEntity.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    throw new ServiceNotAvailableException(">>>>>>>>> WTF: This should be unreachable code!");
  }

  @Override
  public Area getArea(Token userToken, long areaId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas/{areaId}";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
    ParameterizedTypeReference<Area> responseType = new ParameterizedTypeReference<Area>() {};

    try {
      ResponseEntity<Area> responseEntity =
          template.exchange(baseUrl + URI, HttpMethod.GET, requestEntity, responseType, areaId);
      return responseEntity.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    throw new ServiceNotAvailableException(">>>>>>>>> WTF: This should be unreachable code!");
  }

  @Override
  public List<Long> getQuestionIds(Token userToken, long areaId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas/{areaId}/questions";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
    ParameterizedTypeReference<List<Long>> responseType =
        new ParameterizedTypeReference<List<Long>>() {};

    try {
      ResponseEntity<List<Long>> responseEntity =
          template.exchange(baseUrl + URI, HttpMethod.GET, requestEntity, responseType, areaId);
      return responseEntity.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    throw new ServiceNotAvailableException(">>>>>>>>> WTF: This should be unreachable code!");
  }

  @Override
  public Question getQuestion(Token userToken, long areaId, long questionId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas/{areaId}/questions/{questionId}";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
    ParameterizedTypeReference<Question> responseType =
        new ParameterizedTypeReference<Question>() {};

    try {
      ResponseEntity<Question> responseEntity =
          template.exchange(
              baseUrl + URI, HttpMethod.GET, requestEntity, responseType, areaId, questionId);
      return responseEntity.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    throw new ServiceNotAvailableException(">>>>>>>>> WTF: This should be unreachable code!");
  }

  @Override
  public List<Long> getAnswerIds(Token userToken, long areaId, long questionId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas/{areaId}/questions/{questionId}/answers";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
    ParameterizedTypeReference<List<Long>> responseType =
        new ParameterizedTypeReference<List<Long>>() {};

    try {
      ResponseEntity<List<Long>> responseEntity =
          template.exchange(
              baseUrl + URI, HttpMethod.GET, requestEntity, responseType, areaId, questionId);
      return responseEntity.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    throw new ServiceNotAvailableException(">>>>>>>>> WTF: This should be unreachable code!");
  }

  @Override
  public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas/{areaId}/questions/{questionId}/answers/{answerId}";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
    ParameterizedTypeReference<Answer> responseType = new ParameterizedTypeReference<Answer>() {};

    try {
      ResponseEntity<Answer> responseEntity =
          template.exchange(
              baseUrl + URI,
              HttpMethod.GET,
              requestEntity,
              responseType,
              areaId,
              questionId,
              answerId);
      return responseEntity.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    throw new ServiceNotAvailableException(">>>>>>>>> WTF: This should be unreachable code!");
  }

  @Override
  public void updateArea(Token userToken, Area area)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Area> requestEntity = new HttpEntity<>(area, headers);
    try {
      template.put(baseUrl + URI, requestEntity);
    } catch (RestClientException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateQuestion(Token userToken, long areaId, Question question)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas/{areaId}/questions";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Question> requestEntity = new HttpEntity<>(question, headers);
    try {
      template.put(baseUrl + URI, requestEntity, areaId);
    } catch (RestClientException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String URI = "areas/{areaId}/questions/{questionId}/answers";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Answer> requestEntity = new HttpEntity<>(answer, headers);
    try {
      template.put(baseUrl + URI, requestEntity, areaId, questionId);
    } catch (RestClientException e) {
      e.printStackTrace();
    }
  }
}
