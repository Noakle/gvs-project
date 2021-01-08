package de.hhn.it.vs.distribution.qna.provider.wnck.rest;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BDQnAServiceViaRest implements BDQnAService {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(BDQnAServiceViaRest.class);

  private String baseUrl;
  private final RestTemplate template;

  public BDQnAServiceViaRest(final String baseUrl) {
    this.baseUrl = baseUrl;
    template = new RestTemplate();
  }

  private HttpHeaders getHttpHeadersWithUserToken(final Token userToken) {
    // Transport the userToken string in the header
    HttpHeaders headers = new HttpHeaders();
    headers.add("Token", userToken.getToken());
    return headers;
  }

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
  public long createArea(final Token userToken, final Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Area> entity = new HttpEntity<>(area, headers);

    try {
      long result = template.postForObject(baseUrl + uri, entity, Long.class);
      return result;
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    // unreachable code
    return -1;
  }

  @Override
  public long createQuestion(final Token userToken, final long areaId, final Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas/{areaId}/questions";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Question> entity = new HttpEntity<>(question, headers);

    try {
      long result = template.postForObject(baseUrl + uri, entity, Long.class, areaId);
      return result;
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    // unreachable code
    return -1;
  }

  @Override
  public long createAnswer(final Token userToken, final long areaId, final long questionId, final Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas/{areaId}/questions/{questionId}/answers";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Answer> entity = new HttpEntity<>(answer, headers);

    try {
      long result = template.postForObject(baseUrl + uri, entity, Long.class, areaId, questionId);
      return result;
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    // unreachable code
    return -1;
  }

  @Override
  public List<Long> getAreaIds(final Token userToken) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);

    HttpEntity requestEntity = new HttpEntity<Void>(null, headers);

    // Create an object describing the expected Type in the response
    ParameterizedTypeReference<List<Long>> responseType
            = new ParameterizedTypeReference<List<Long>>() {
    };


    try {
      ResponseEntity<List<Long>> response = template.exchange(baseUrl + uri, HttpMethod.GET,
              requestEntity, responseType);
      return response.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    // unreachable code
    return null;
  }

  @Override
  public Area getArea(final Token userToken, final long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas/{areaId}";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity requestEntity = new HttpEntity<Area>(null, headers);

    try {
      ResponseEntity<Area> responseEntity = template.exchange(baseUrl + uri, HttpMethod.GET,
              requestEntity, Area.class, areaId);
      return responseEntity.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    // unreachable code
    return null;
  }

  @Override
  public List<Long> getQuestionIds(final Token userToken, final long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas/{areaId}/questions";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity requestEntity = new HttpEntity<Void>(null, headers);

    ParameterizedTypeReference<List<Long>> responseType
            = new ParameterizedTypeReference<List<Long>>() {
    };

    try {
      ResponseEntity<List<Long>> response = template.exchange(baseUrl + uri, HttpMethod.GET,
              requestEntity, responseType, areaId);
      return response.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    // unreachable code
    return null;
  }

  @Override
  public Question getQuestion(final Token userToken, final long areaId, final long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas/{areaId}/questions/{questionId}";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity requestEntity = new HttpEntity<Void>(null, headers);

    try {
      ResponseEntity<Question> responseEntity = template.exchange(baseUrl + uri, HttpMethod.GET,
              requestEntity, Question.class, areaId, questionId);
      return responseEntity.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    // unreachable code
    return null;
  }

  @Override
  public List<Long> getAnswerIds(final Token userToken, final long areaId, final long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas/{areaId}/questions/{questionId}/answers";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity requestEntity = new HttpEntity<Void>(null, headers);

    ParameterizedTypeReference<List<Long>> responseType
            = new ParameterizedTypeReference<List<Long>>() {
    };


    try {
      ResponseEntity<List<Long>> response = template.exchange(baseUrl + uri, HttpMethod.GET,
              requestEntity, responseType, areaId, questionId);
      return response.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    // unreachable code
    return null;
  }

  @Override
  public Answer getAnswer(final Token userToken, final long areaId, final long questionId, final long answerId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas/{areaId}/questions/{questionId}/answers/{answerId}";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity requestEntity = new HttpEntity<Void>(null, headers);

    try {
      ResponseEntity<Answer> responseEntity = template.exchange(baseUrl + uri, HttpMethod.GET,
              requestEntity, Answer.class, areaId, questionId, answerId);
      return responseEntity.getBody();
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
    // unreachable code
    return null;
  }

  @Override
  public void updateArea(final Token userToken, final Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas/{areaId}";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Area> entity = new HttpEntity<>(area, headers);

    try {
      template.put(baseUrl + uri, entity, area.getId());
      return;
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
  }

  @Override
  public void updateQuestion(final Token userToken, final long areaId, final Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas/{areaId}/questions/{questionId}";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Question> entity = new HttpEntity<>(question, headers);

    try {
      template.put(baseUrl + uri, entity, areaId, question.getId());
      return;
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }
  }

  @Override
  public void updateAnswer(final Token userToken, final long areaId, final long questionId, final Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    final String uri = "areas/{areaId}/questions/{questionId}/answer/{answerId}";
    HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
    HttpEntity<Answer> entity = new HttpEntity<>(answer, headers);

    try {
      template.put(baseUrl + uri, entity, areaId, questionId, answer.getId());
      return;
    } catch (HttpClientErrorException e) {
      rethrowException(e);
    }

  }
}
