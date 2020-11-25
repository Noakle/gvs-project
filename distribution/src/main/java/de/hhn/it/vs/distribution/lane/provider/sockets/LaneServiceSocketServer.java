package de.hhn.it.vs.distribution.lane.provider.sockets;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.lane.provider.BDlaneServiceViaSockets;
import de.hhn.it.vs.distribution.sockets.Request;

public class LaneServiceSocketServer {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(LaneServiceSocketServer.class);

  private static BDlaneServiceViaSockets bDlaneServiceViaSockets;
  //private static LaneServiceServeClient laneServiceServeClient = new LaneServiceServeClient();

  public static void main(String[] args)
      throws ServiceNotAvailableException, IllegalParameterException {

    bDlaneServiceViaSockets = new BDlaneServiceViaSockets("localhost", 8000);
    bDlaneServiceViaSockets.connectToService();
    logger.info("Connected to Service.");
    bDlaneServiceViaSockets.sendAndGetResponse(new Request("test"));
    bDlaneServiceViaSockets.disconnectFromService();
    logger.info("Disconnected from Service.");

  }

}
