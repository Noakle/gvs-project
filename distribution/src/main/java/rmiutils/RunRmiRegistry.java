package rmiutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

public class RunRmiRegistry {
  /**
   * Handy constant to have a class name which can easily be refactored.
   **/
  public static final String MY_NAME = RunRmiRegistry.class.getName();
  /**
   * Standard class logger
   **/
  private static Logger logger = Logger.getLogger(MY_NAME);
  private static final int PORT = 1099;

  /**
   * Helper class to run a rmiregistry from within an eclipse project. With
   * this approach the rmiregistry has all class files pf the project in its
   * CLASSPATH.
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    Registry registry = LocateRegistry.createRegistry(PORT);

    BufferedReader keyboard = new BufferedReader(new InputStreamReader(
            System.in));

    Remote entfernteReferenz = null;

    while (true) {
      System.out.println("Your command, Master (list, end):");
      String line = keyboard.readLine();
      if ("list".equals(line)) {
        String[] list = registry.list();
        System.out.println("rmiregistry - list : " + list.length
                + " entries.");
        for (String string : list) {
          try {
            entfernteReferenz = registry.lookup(string);
          } catch (NotBoundException e) {
            e.printStackTrace();
          }
          System.out.println("[" + string + "] " + entfernteReferenz);
        }
        continue;
      }

      if ("end".equals(line)) {
        System.out.println("It was a pleasure to serve you, Master.");
        break;
      }
    }

  }
}
