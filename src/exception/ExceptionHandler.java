package exception;

public class ExceptionHandler {
    public static void handle(Exception e, String context) {
        System.err.println("Ошибка в " + context + ": " + e.getMessage());
    }
}
