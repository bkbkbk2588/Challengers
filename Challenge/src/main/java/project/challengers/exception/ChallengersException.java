package project.challengers.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ChallengersException extends ChallengersRuntimeException{
    String msgKey;
    HttpStatus status;
    String[] args;

    public ChallengersException(String msgKey) {
        super(msgKey);
        this.msgKey = msgKey;
    }

    public ChallengersException(String msgKey, String[] args) {
        super(msgKey);
        this.msgKey = msgKey;
        this.args = args;
    }

    public ChallengersException(HttpStatus status, String msgKey) {
        super(msgKey);
        this.msgKey = msgKey;
        this.status = status;
    }

    public ChallengersException(HttpStatus status, String msgKey, String[] args) {
        super(msgKey);
        this.msgKey = msgKey;
        this.status = status;
        this.args = args;
    }

    public ChallengersException(HttpStatus status, String msgKey, Throwable cause) {
        super(msgKey, cause);
        this.msgKey = msgKey;
        this.status = status;
    }


    public ChallengersException(HttpStatus status, String msgKey, String[] args, Throwable cause) {
        super(msgKey, cause);
        this.msgKey = msgKey;
        this.status = status;
        this.args = args;
    }
}
