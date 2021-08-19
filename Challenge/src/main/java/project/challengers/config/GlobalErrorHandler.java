package project.challengers.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import project.challengers.exception.ChallengersException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Locale;

@Configuration
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @Autowired
    private MessageSource messageSource;

    private ObjectMapper objectMapper;

    public GlobalErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
        DataBuffer dataBuffer = null;
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (throwable instanceof ChallengersException) {
            try {
                ChallengersException exception = (ChallengersException) throwable;

                String message = messageSource.getMessage(exception.getMsgKey(), exception.getArgs(), Locale.getDefault());
                logger.debug("Ex Msg:{}, MsgKey:{},Args: {}", message, exception.getMsgKey(), exception.getArgs());

                dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(
                        new ChallengersHttpError(exception.getStatus().value(), //status
                                exception.getStatus().name(), // error
                                message) // message
                ));
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("{}", e);
                dataBuffer = bufferFactory.wrap("Interal Server Error 0x01".getBytes(StandardCharsets.UTF_8));
            }
        } else {
            logger.error("ChallengersException Other Exception", throwable);
            throwable.printStackTrace(System.err);
            try {
                dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(
                        new ChallengersHttpError(
                                throwable instanceof ResponseStatusException ? ((ResponseStatusException) throwable).getStatus().value()
                                        : HttpStatus.INTERNAL_SERVER_ERROR.value()
                                , throwable instanceof ResponseStatusException ? ((ResponseStatusException) throwable).getStatus().name()
                                : HttpStatus.INTERNAL_SERVER_ERROR.name(),
                                throwable.getMessage())
                ));
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("{}", e);
                dataBuffer = bufferFactory.wrap("Internal Server Error 0x02".getBytes(StandardCharsets.UTF_8));
            }
        }

        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    public class ChallengersHttpError {
        private LocalDateTime timestamp = LocalDateTime.now();
        private int status;
        private String error;
        private String message;

        ChallengersHttpError(int status, String error, String message) {
            this.status = status;
            this.error = error;
            this.message = message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}