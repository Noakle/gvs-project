package de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.TokenFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The AbstractTokenFactory can be derived to create a TokenFactory for objects of a dedicated
 * class.
 * <p/>
 * Created by wnck on 17/10/16.
 */
public abstract class AbstractTokenFactory implements TokenFactory {


  /**
   * Builds a Token based on a given, unique String.
   *
   * @param tokenString unique String for this object
   * @return Token containing a Hash based on the unique String
   */
  private Token buildToken(final String tokenString) {
    String hashedTokenString = hashString(tokenString);
    Token retval = new Token(hashedTokenString);
    return retval;
  }

  /**
   * Creates a Token, based on the generateTokenString method of a derived class.
   *
   * @param object Object the token is for
   * @return Token based on the object. Identical objects should create identical tokens.
   */
  public final Token createToken(final Object object) {
    String tokenString = generateTokenString(object);
    Token retval = buildToken(tokenString);
    return retval;
  }

  /**
   * Must generate a unique string which cannot reproduce the original data, e.g. by using a hash
   * function.
   * Same object data should produces same tokens.
   *
   * @param object input to the mechanism to generate always the same token based on the same object
   * @return resulting string
   */
  protected abstract String generateTokenString(Object object);

  /**
   * Hashes the given string and returns a string in hex.
   *
   * @param data string identifying the token unhashed und unhexed
   * @return hashed string in hex
   */
  public final String hashString(final String data) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-256");
      md.update(data.getBytes("UTF-8"));
      byte[] digest = md.digest();

      return String.format("%064x", new java.math.BigInteger(1, digest));

    } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
      exception.printStackTrace();
      throw new UnsupportedOperationException("Either the algorithm or the encoding is not "
              + "supported.", exception);
    }

  }
}
