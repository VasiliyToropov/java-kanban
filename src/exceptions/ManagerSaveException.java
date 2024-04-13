package exceptions;

public class ManagerSaveException extends Exception {
    String message;

    public ManagerSaveException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
