package kuroseross.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Simple client to demonstrate easy TCP communication sending strings
 */
public class TCPClient {
  /** Logger, named using the complete class name. */
  private static final Logger logger = Logger.getLogger(TCPClient.class.getName());

  public static void main(String[] args) throws Exception {
    String sentence;
    String modifiedSentence;

    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
        System.in));

    Socket clientSocket = new Socket("127.0.0.1", TCPServer.PORT);

    DataOutputStream outToServer = new DataOutputStream(
        clientSocket.getOutputStream());

    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
        clientSocket.getInputStream()));

    System.out.println("Please give me a line!");
    sentence = inFromUser.readLine();

    outToServer.writeBytes(sentence + '\n');

    modifiedSentence = inFromServer.readLine();

    System.out.println("FROM SERVER: " + modifiedSentence);

    clientSocket.close();

  }
}
