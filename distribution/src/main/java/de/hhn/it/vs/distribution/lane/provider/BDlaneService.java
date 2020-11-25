package de.hhn.it.vs.distribution.lane.provider;

import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

public interface BDlaneService {

  /**
   * Connects to service.
   *
   * @throws ServiceNotAvailableException
   */
  void connectToService() throws ServiceNotAvailableException;

  /**
   * Disconnects from service.
   */
  void disconnectFromService();

  Response sendAndGetResponse(final Request request) throws ServiceNotAvailableException;

}
