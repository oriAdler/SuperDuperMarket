package exception;

public class invalidLocationException extends RuntimeException {
    private final String errorMessage;

    public invalidLocationException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
