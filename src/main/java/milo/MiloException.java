package milo;

/** Represents an expected error while processing a Milo command or task. */
public class MiloException extends Exception {
    /** Creates an exception with a user-facing explanation. */
    public MiloException(String message) {
        super(message);
    }
}
