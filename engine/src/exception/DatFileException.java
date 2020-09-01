package exception;

public class DatFileException extends RuntimeException{
    private String errorMessage;

    public DatFileException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
