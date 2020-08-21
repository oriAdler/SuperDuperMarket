package course.java.sdm.exception;

public class invalidCustomerLocationException extends RuntimeException {
    private final String errorMessage;

    public invalidCustomerLocationException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
