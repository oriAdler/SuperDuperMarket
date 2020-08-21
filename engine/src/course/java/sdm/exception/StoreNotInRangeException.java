package course.java.sdm.exception;

import java.awt.*;

public class StoreNotInRangeException extends RuntimeException{
    private final String storeName;
    private final Point location;
    private final String EXCEPTION_MESSAGE = "Invalid File: %s location is [%s,%s] should be between [1,50].";

    public StoreNotInRangeException(String storeName, Point location) {
        this.storeName = storeName;
        this.location = location;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, storeName, location.getX(), location.getY());
    }
}
