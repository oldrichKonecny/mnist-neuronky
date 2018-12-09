package eu.olda.mnist.exceptions;

/**
 * Created by olda on 05.12.2018
 */
public class InvalidArgumentException extends RuntimeException {

  public InvalidArgumentException(String message) {
    super(message);
  }

  public InvalidArgumentException(String message, Throwable cause) {
    super(message, cause);
  }
}
