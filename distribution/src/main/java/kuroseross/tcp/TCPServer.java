package kuroseross.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Simple server to demonstrate easy TCP communication sending strings
 */
public class TCPServer {
  /**
   * Logger, named using the complete class name.
   */
  private static final Logger logger = Logger.getLogger(TCPServer.class.getName());

  /**
   * Port to be connected to. Has to be public to allow the client to use this port number.
   */
  public static final int PORT = 1099;

  public static void main(String argv[]) throws Exception {
    String clientSentence;
    String capitalizedSentence;

    @SuppressWarnings("resource")
    ServerSocket welcomeSocket = new ServerSocket(PORT);

    while (true) {

      logger.info("Ready to accept connections ...");
      Socket connectionSocket = welcomeSocket.accept();

      logger.info("New connection: " + connectionSocket);
      BufferedReader inFromClient = new BufferedReader(
              new InputStreamReader(connectionSocket.getInputStream()));

      DataOutputStream outToClient = new DataOutputStream(
              connectionSocket.getOutputStream());

      clientSentence = inFromClient.readLine();
      logger.info("convert " + clientSentence + " to uppercase");

      capitalizedSentence = clientSentence.toUpperCase() + '\n';

      outToClient.writeBytes(capitalizedSentence);

      connectionSocket.close();
    }
  }
}
