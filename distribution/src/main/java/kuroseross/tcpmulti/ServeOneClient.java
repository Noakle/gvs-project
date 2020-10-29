package kuroseross.tcpmulti;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Simple Thread example to work an a single request
 */
public class ServeOneClient extends Thread {
  /**
   * Logger, named using the complete class name.
   */
  Logger logger = Logger.getLogger(ServeOneClient.class.getName());

  private Socket socket;

  public ServeOneClient(Socket socket) {
    this.socket = socket;
    start();
  }

  @Override
  public void run() {
    String clientSentence;

    BufferedReader inFromClient;
    try {
      inFromClient = new BufferedReader(new InputStreamReader(
              this.socket.getInputStream()));
      DataOutputStream outToClient = new DataOutputStream(
              this.socket.getOutputStream());

      clientSentence = inFromClient.readLine();
      logger.info("convert " + clientSentence + " to uppercase");

      String capitalizedSentence = clientSentence.toUpperCase() + '\n';

      outToClient.writeBytes(capitalizedSentence);

      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
