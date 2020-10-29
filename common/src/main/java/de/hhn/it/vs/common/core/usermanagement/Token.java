package de.hhn.it.vs.common.core.usermanagement;

import java.io.Serializable;
import java.util.Objects;

public class Token implements Serializable {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(Token.class);

  private String token;

  public Token() {
  }

  public Token(final String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof Token)) return false;
    Token token1 = (Token) o;
    return getToken().equals(token1.getToken());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getToken());
  }

  @Override
  public String toString() {
    return "Token{" +
            "token='" + token + '\'' +
            '}';
  }
}
