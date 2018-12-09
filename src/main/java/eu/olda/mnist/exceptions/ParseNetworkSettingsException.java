package eu.olda.mnist.exceptions;

/**
 * Created by olda on 05.12.2018
 */
public class ParseNetworkSettingsException extends RuntimeException {

  public ParseNetworkSettingsException(String message) {
    super(message);
  }

  public ParseNetworkSettingsException(String message, Throwable cause) {
    super(message, cause);
  }
}
