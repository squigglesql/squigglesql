package network.tide.squiggle.util;

public class TooManyRecordsException extends RuntimeException {

    public TooManyRecordsException() {
        super("Expected to retrieve 0 or 1 records from database, got more.");
    }
}
