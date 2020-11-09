package de.hhn.it.vs.distribution.qna.provider.mrvn.data;

/**
 * Exception indicating that a user with the specified name cannot be found.
 */
public class NoSuchRegisteredUserException extends Exception {

  /**
   * Default constructor.
   */
  public NoSuchRegisteredUserException() {}

  /**
   * Constructor providing some error message.
   *
   * @param s The error message to be displayed
   */
  public NoSuchRegisteredUserException(String s) {
    System.err.println(s);
  }
}
