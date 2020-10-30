package de.hhn.it.vs.distribution.qna.provider.sgds.sockets;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BDSgDsServiceViaSockets {

  private String hostname;
  private int portnumber;
  private Socket socket;
  private ObjectOutputStream outToServer;
  private ObjectInputStream inFromServer;

  public BDSgDsServiceViaSockets(final String hostname,final int portnumber){
    this.hostname = hostname;
    this.portnumber = portnumber;
  }

  public void connectToService() throws IOException {
    socket = new Socket(hostname, portnumber);
    outToServer = new ObjectOutputStream(socket.getOutputStream());
    inFromServer = new ObjectInputStream(socket.getInputStream());
    }

  public void disconnectFromService() throws IOException {
    outToServer.close();
    inFromServer.close();
    socket.close();
  }

  public Response sendRequestAndGetResponse(final String method)
      throws IllegalParameterException, IOException, ClassNotFoundException {
    Request request = new Request(method);
    connectToService();
    outToServer.writeObject(request);
    Response response = (Response)inFromServer.readObject();
    disconnectFromService();
    return response;
  }

}
