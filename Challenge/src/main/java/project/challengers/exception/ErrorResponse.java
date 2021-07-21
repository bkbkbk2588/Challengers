package project.challengers.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ChallengersException challengersException) {
        return ResponseEntity
                .status(challengersException.getStatus())
                .body(ErrorResponse.builder()
                        .status(challengersException.getStatus().value())
                        .error(challengersException.getStatus().name())
                        .message(challengersException.getMessage())
                        .build()
                );
    }
}
