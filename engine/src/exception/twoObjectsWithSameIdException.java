package exception;

public class twoObjectsWithSameIdException extends RuntimeException {

    private final String objectName, first, second;
    private final Integer id;

    public twoObjectsWithSameIdException(String objectName, String first, String second, Integer id) {
        this.objectName = objectName;
        this.first = first;
        this.second = second;
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("File not loaded successfully: %s '%s' and '%s' have the same id: '%d'",
                objectName, first, second, id);
    }
}
