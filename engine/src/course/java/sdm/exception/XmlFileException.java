package course.java.sdm.exception;

public class XmlFileException extends RuntimeException {

    private final String errorMessage;

    public XmlFileException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
