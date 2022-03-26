package io.github.xinput.commons.exception;

public class HttpUtilException extends RuntimeException {

  public HttpUtilException() {
  }

  public HttpUtilException(String message) {
    super(message);
  }

  public HttpUtilException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpUtilException(Throwable cause) {
    super(cause);
  }

  public HttpUtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
