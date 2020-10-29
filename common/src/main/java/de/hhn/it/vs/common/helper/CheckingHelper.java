package de.hhn.it.vs.common.helper;


import de.hhn.it.vs.common.exceptions.IllegalParameterException;

import java.io.Serializable;

/**
 * Helper class to check simple things.
 */
public class CheckingHelper {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(CheckingHelper.class);


  /**
   * Checks if the string parameter has some readable string within.
   *
   * @param string          string to be checked
   * @param nameInException name of the parameter to be shown in the exception
   * @throws IllegalParameterException if the string to be checked is a null reference or
   *                                   contains no or only whitespace chars
   */
  public static void assertThatIsReadableString(String string, String nameInException) throws
          IllegalParameterException {
    if (string == null) {
      String message = "String for " + nameInException + " is null reference.";
      logger.warn(message);
      throw new IllegalParameterException(message);
    }

    if (string.trim().length() == 0) {
      String message = "String  " + nameInException + " consists only of whitespace.";
      logger.warn(message);
      throw new IllegalParameterException(message);

    }
  }

  /**
   * Checks if the given object reference is a null reference.
   *
   * @param object          reference to be checked
   * @param nameInException name of the object within an exception
   * @throws IllegalParameterException if the object is a null reference
   */
  public static void assertThatIsNotNull(Object object, String nameInException) throws
          IllegalParameterException {
    if (object == null) {
      String message = "Reference for " + nameInException + " is null reference.";
      logger.warn(message);
      throw new IllegalParameterException(message);
    }
  }

  /**
   * Checks if the given object implements the Serializable interface.
   *
   * @param object          Object to be checked
   * @param nameInException parameter name for the exception
   * @throws IllegalParameterException if the object is not serializable
   */
  public static void assertThatIsSerializable(Object object, String nameInException) throws
          IllegalParameterException {
    if (object == null) {
      return;
    }
    if (!(object instanceof Serializable)) {
      String className = object.getClass().getName();
      String message = "Object " + nameInException + " of class " + className + " is not Serializable.";
      logger.warn(message);
      throw new IllegalParameterException(message);
    }
  }
}
