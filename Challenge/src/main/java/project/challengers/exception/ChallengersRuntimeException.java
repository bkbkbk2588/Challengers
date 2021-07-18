package project.challengers.exception;

public class ChallengersRuntimeException extends RuntimeException{
    public ChallengersRuntimeException(String message) {
        super(message);
    }
    public ChallengersRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }


    public ChallengersRuntimeException(Throwable cause) {
        super(cause);
    }


    protected ChallengersRuntimeException(String message, Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
