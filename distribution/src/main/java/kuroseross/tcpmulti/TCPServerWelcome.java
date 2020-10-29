package kuroseross.tcpmulti;

import kuroseross.tcp.TCPServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 *
 * Launcher which accepts connections and delegates the processing of this connection to a new
 * thread.
 *
 * @author wnck
 * 
 */
public class TCPServerWelcome {
  /** Logger, named using the complete class name. */
  private static final Logger logger = Logger.getLogger(TCPServerWelcome.class.getName());

  public static void main(String[] argv) throws Exception {
    @SuppressWarnings("resource")
    ServerSocket welcomeSocket = new ServerSocket(TCPServer.PORT);
    logger.info("Server ready ...");

    while (true) {

      logger.info("Ready to accept connections ...");

      Socket connectionSocket = welcomeSocket.accept();
      ServeOneClient thread = new ServeOneClient(connectionSocket);
      logger.info("new connection " + connectionSocket + " for thread " + thread);
    }
  }
}
