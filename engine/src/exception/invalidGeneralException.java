package exception;

public class invalidGeneralException extends RuntimeException {
    private final String exceptionMessage;

    public invalidGeneralException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
