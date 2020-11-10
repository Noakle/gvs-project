package de.hhn.it.vs.distribution.qna.provider.mntl.sockets;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;

import java.io.IOException;
import java.net.Socket;

/**
 * Class Service Serve One Client
 * @author Leibl, Nauendorf
 * @version 2020-11-10
 */
public class QnAServiceServeOneClient extends AbstractServeOneClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(QnAServiceServeOneClient.class);

  public static final String CREATE_ANSWER = "managment";
  public static final String CREATE_AREA = "managment.create_area";
  public static final String PARAM_USER_TOKEN = "param.usertoken";
  public static final String PARAM_AREA_ID = "param.area_id";
  public static final String PARAM_QUESTION = "param.question";
  public static final String PARAM_ANSWER = "param.answer";
  public static final String PARAM_AREA = "param.area";
  public static final String GET_AREA_IDS = "get.area_ids";
  public static final String GET_AREA = "get.area";
  public static final String GET_QUESTION = "get.question";
  public static final String GET_ANSWER = "get.answer";
  public static final String UPDATE_AREA = "update.area";

  public static final String UPDATE_QUESTION = "update.question";
  public static final String UPDATE_ANSWER = "update.answer";





  public QnAServiceServeOneClient( final Socket socket, final Object service) throws IOException,
    IllegalParameterException{
  super (socket, service);
    }

  }

