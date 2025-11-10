package exception;

public class InvalidAccountFormatException extends RuntimeException {
    public InvalidAccountFormatException(String message) {
        super(message);
    }
}
