package eu.olda.mnist.exceptions;

/**
 * Created by olda on 09.12.2018
 */
public class MnistGeneralException extends RuntimeException {
  public MnistGeneralException(String message) {
    super(message);
  }

  public MnistGeneralException(String message, Throwable cause) {
    super(message, cause);
  }
}
