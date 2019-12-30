package exception;

public class SolveTimeException extends RuntimeException {
    String message;

    public SolveTimeException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
