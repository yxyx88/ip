package milo;

/** Represents an expected error while processing a Milo command or task. */
public class MiloException extends Exception {
    private static final long serialVersionUID = 1L;

    /** Creates an exception with a user-facing explanation. */
    public MiloException(String message) {
        super(message);
    }
}
