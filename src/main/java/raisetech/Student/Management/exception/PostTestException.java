package raisetech.student.management.exception;

public class PostTestException extends Exception {

  public PostTestException() {
    super();
  }

  public PostTestException(String message) {
    super(message);
  }

  public PostTestException(String message, Throwable cause) {
    super(message, cause);
  }

  public PostTestException(Throwable cause) {
    super(cause);
  }
}