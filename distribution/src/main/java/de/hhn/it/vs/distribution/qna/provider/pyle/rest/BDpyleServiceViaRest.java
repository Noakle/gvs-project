package de.hhn.it.vs.distribution.qna.provider.pyle.rest;

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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BDpyleServiceViaRest  implements BDQnAService {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDpyleServiceViaRest.class);

    private String baseUrl;
    private final RestTemplate template;

    public BDpyleServiceViaRest(final String baseUrl) {
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
    public long createArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException
    {
       try {
           HttpHeaders areaheader = this.getHttpHeadersWithUserToken(userToken);
           HttpEntity<Area> areaEntity = new HttpEntity<Area>(area, areaheader);
           final String uri = "areauri";

           return template.postForObject(baseUrl + uri, areaEntity, long.class);
       }catch(HttpClientErrorException e)
       {
           rethrowException(e);
       }
       throw  new ServiceNotAvailableException("Rest probleme");
    }


    @Override
    public long createQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException
    {
        try {
            final String uri = "areauri";
            HttpHeaders areaheader = this.getHttpHeadersWithUserToken(userToken);
            HttpEntity<Question> areaEntity = new HttpEntity<Question>(question, areaheader);
            return template.postForObject(baseUrl + uri, areaEntity, long.class, areaId);
        }catch(HttpClientErrorException e)
    {
        rethrowException(e);
    }
       throw  new ServiceNotAvailableException("Rest probleme");


    }

    @Override
    public long createAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException
    {
        try {
            final String uri = "areauri";
            HttpHeaders areaheader = this.getHttpHeadersWithUserToken(userToken);
            HttpEntity<Answer> areaEntity = new HttpEntity<Answer>(answer,areaheader );
            return template.postForObject(baseUrl + uri, areaEntity, long.class, areaId,questionId);
        }catch(HttpClientErrorException e)
        {
            rethrowException(e);
        }
        throw  new ServiceNotAvailableException("Rest probleme");

    }

    @Override
    public List<Long> getAreaIds(Token userToken) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException
    {
        try {
            HttpHeaders areaheader = this.getHttpHeadersWithUserToken(userToken);
            HttpEntity areaEntity = new HttpEntity<>(null, areaheader);

            final String uri = "areauri";
            ParameterizedTypeReference<List<Long>> convertTypeReturn = new ParameterizedTypeReference<List<Long>>() {
            };

            return template.exchange(baseUrl + uri, HttpMethod.GET, areaEntity, convertTypeReturn).getBody();
        } catch(HttpClientErrorException e)
        {
            rethrowException(e);
        }
        throw  new ServiceNotAvailableException("Rest probleme");
    }

    @Override
    public Area getArea(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException
    {
        try {
            final String uri = "areauri";
            HttpHeaders areaheader = this.getHttpHeadersWithUserToken(userToken);
            HttpEntity areaEntity = new HttpEntity<>(null, areaheader);
            return template.exchange(baseUrl + uri, HttpMethod.GET, areaEntity, Area.class).getBody();
        } catch(HttpClientErrorException e)
        {
            rethrowException(e);
        }
        throw  new ServiceNotAvailableException("Rest probleme");
    }

    @Override
    public List<Long> getQuestionIds(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        return null;
    }

    @Override
    public Question getQuestion(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        return null;
    }

    @Override
    public List<Long> getAnswerIds(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        return null;
    }

    @Override
    public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        return null;
    }

    @Override
    public void updateArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {

    }

    @Override
    public void updateQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {

    }

    @Override
    public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {

    }
}
