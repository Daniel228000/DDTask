package exceptions;

public class UnassignedFieldException extends RuntimeException{
    public UnassignedFieldException(String message) {
        super(message);
    }
}
