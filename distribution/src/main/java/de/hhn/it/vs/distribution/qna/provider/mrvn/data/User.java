package de.hhn.it.vs.distribution.qna.provider.mrvn.data;

/**
 * Simple user representation only containing a String for each: email, password, name.
 */
public class User {

  private String email;
  private String password;
  private String name;

  /**
   * Constructor.
   *
   * @param email String representation of the email
   * @param password String representation of the password
   * @param name String representation of the name
   */
  public User(String email, String password, String name) {
    this.email = email;
    this.password = password;
    this.name = name;
  }

  /**
   * Getter for the email.
   *
   * @return String representation of the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Getter for the password.
   *
   * @return String representation of the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Getter for the name.
   *
   * @return String representation of the name
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for the email.
   *
   * @param email String representation of the new email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Setter for the password.
   *
   * @param password String representation of the new password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Setter for the name.
   *
   * @param name String representation of the new name
   */
  public void setName(String name) {
    this.name = name;
  }
}
