package lv.mintos.demo.DemoApp.err;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AppLogicException extends RuntimeException {

    private final HttpStatus status;

    public AppLogicException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
