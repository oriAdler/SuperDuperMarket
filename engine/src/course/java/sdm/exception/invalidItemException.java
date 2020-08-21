package course.java.sdm.exception;

public class invalidItemException extends RuntimeException {
    private final String exceptionMessage;

    public invalidItemException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
