package de.hhn.it.vs.distribution.qna.provider.fdkh.rest;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import de.hhn.it.vs.common.core.usermanagement.Token;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BDFdkhServiceViaRest implements BDQnAService {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDFdkhServiceViaRest.class);

    public static final String WTF_THIS_SHOULD_BE_UNREACHABLE_CODE = ">>>>>>> WTF: This should be unreachable code.";

    private String baseURL;
    private final RestTemplate restTemplate;

    public BDFdkhServiceViaRest(final String baseURL) {
        this.baseURL = baseURL;
        restTemplate = new RestTemplate();
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
    public long createArea(final Token userToken, final Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("createArea");
        final String uri = "area";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Area> requestEntry = new HttpEntity<>(area, headers);

        try {
            return restTemplate.postForObject(baseURL + uri, requestEntry, long.class);
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }

    @Override
    public long createQuestion(final Token userToken, final long areaId, final Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("createQuestion");
        final String uri = "areas/{areaId}/question";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Question> requestEntry = new HttpEntity<>(question, headers);

        try {
            return restTemplate.postForObject(baseURL + uri, requestEntry, long.class, areaId);
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }

    @Override
    public long createAnswer(final Token userToken, final long areaId, final long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("createAnswer");
        final String uri = "areas/{areaId}/questions/{questionId}/answer";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Answer> requestEntry = new HttpEntity<>(answer, headers);

        try {
            return restTemplate.postForObject(baseURL + uri, requestEntry, long.class, areaId, questionId);
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }

    @Override
    public List<Long> getAreaIds(final Token userToken) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("getAreaIds");
        final String uri = "areas";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Void> requestEntry = new HttpEntity<>(null, headers);

        ParameterizedTypeReference<List<Long>> responsType = new ParameterizedTypeReference<List<Long>>() {
        };
        try {
            ResponseEntity<List<Long>> responseEntity = restTemplate.exchange(baseURL + uri, HttpMethod.GET, requestEntry, responsType);

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }

    @Override
    public Area getArea(final Token userToken, final long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("getArea");
        final String uri = "areas/{areaId}";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Void> requestEntry = new HttpEntity<>(null, headers);

        ParameterizedTypeReference<Area> responsType = new ParameterizedTypeReference<Area>() {
        };
        try {
            ResponseEntity<Area> responseEntity = restTemplate.exchange(baseURL + uri, HttpMethod.GET, requestEntry, responsType, areaId);

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }

    @Override
    public List<Long> getQuestionIds(final Token userToken, final long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("getQuestionIds");
        final String uri = "areas/{areaId}/questions";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Void> requestEntry = new HttpEntity<>(null, headers);

        ParameterizedTypeReference<List<Long>> responsType = new ParameterizedTypeReference<List<Long>>() {
        };
        try {
            ResponseEntity<List<Long>> responseEntity = restTemplate.exchange(baseURL + uri, HttpMethod.GET, requestEntry, responsType, areaId);

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }

    @Override
    public Question getQuestion(final Token userToken, final long areaId, final long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("getQuestion");
        final String uri = "areas/{areaId}/questions/{questionId}";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Void> requestEntry = new HttpEntity<>(null, headers);

        ParameterizedTypeReference<Question> responsType = new ParameterizedTypeReference<Question>() {
        };
        try {
            ResponseEntity<Question> responseEntity = restTemplate.exchange(baseURL + uri, HttpMethod.GET, requestEntry, responsType, areaId, questionId);

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }

    @Override
    public List<Long> getAnswerIds(final Token userToken, final long areaId, final long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("getAnswerIds");
        final String uri = "areas/{areaId}/questions/{questionId}/answers";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Void> requestEntry = new HttpEntity<>(null, headers);

        ParameterizedTypeReference<List<Long>> responsType = new ParameterizedTypeReference<List<Long>>() {
        };
        try {
            ResponseEntity<List<Long>> responseEntity = restTemplate.exchange(baseURL + uri, HttpMethod.GET, requestEntry, responsType, areaId, questionId);

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }

    @Override
    public Answer getAnswer(final Token userToken, final long areaId, final long questionId, final long answerId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("getAnswer");
        final String uri = "areas/{areaId}/questions/{questionId}/answers/{answerId}";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Void> requestEntry = new HttpEntity<>(null, headers);

        ParameterizedTypeReference<Answer> responsType = new ParameterizedTypeReference<Answer>() {
        };
        try {
            ResponseEntity<Answer> responseEntity = restTemplate.exchange(baseURL + uri, HttpMethod.GET, requestEntry, responsType, areaId, questionId, answerId);

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }

    @Override
    public void updateArea(final Token userToken, final Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("updateArea");
        final String uri = "areas/status";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Area> requestEntry = new HttpEntity<>(area, headers);

        ParameterizedTypeReference<Void> responsType = new ParameterizedTypeReference<Void>() {
        };
        try {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(baseURL + uri, HttpMethod.PUT, requestEntry, responsType);
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }

    @Override
    public void updateQuestion(final Token userToken, final long areaId, final Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("updateQuestion");
        final String uri = "areas/{areaId}/questions/status";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Question> requestEntry = new HttpEntity<>(question, headers);

        ParameterizedTypeReference<Void> responsType = new ParameterizedTypeReference<Void>() {
        };
        try {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(baseURL + uri, HttpMethod.PUT, requestEntry, responsType, areaId);
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }

    @Override
    public void updateAnswer(final Token userToken, final long areaId, final long questionId, final Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        logger.info("updateAnswer");
        final String uri = "areas/{areaId}/questions/{questionId}/answers/status";

        HttpHeaders headers = getHttpHeadersWithUserToken(userToken);
        HttpEntity<Answer> requestEntry = new HttpEntity<>(answer, headers);

        ParameterizedTypeReference<Void> responsType = new ParameterizedTypeReference<Void>() {
        };
        try {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(baseURL + uri, HttpMethod.PUT, requestEntry, responsType, areaId, questionId);
        } catch (HttpClientErrorException e) {
            rethrowException(e);
        }
        throw new ServiceNotAvailableException(WTF_THIS_SHOULD_BE_UNREACHABLE_CODE);
    }
}

