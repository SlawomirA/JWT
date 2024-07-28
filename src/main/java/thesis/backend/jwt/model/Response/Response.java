package thesis.backend.jwt.model.Response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import thesis.backend.jwt.utils.Consts;

import java.util.Date;

/**
 * Custom object for response class.
 * message - custom message returned by endpoint
 */

@Slf4j
@Getter
@Setter
public class Response<T> {
    private String message;
    private Date date;
    private int code;
    private String stacktrace;
    private T containedObject;


    public Response() {
        System.out.println();
        date = new Date();
        log.debug(this.getClass() + Consts.INITIALIZED);
    }

    public Response(final String message, final int code, final String stacktrace, final T containedObject) {
        this.message = message;
        this.date = new Date();
        this.code = code;
        this.stacktrace = stacktrace;
        this.containedObject = containedObject;
        log.debug(this.getClass() + Consts.INITIALIZED);
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                ", date=" + date +
                ", code=" + code +
                ", stacktrace='" + stacktrace + '\'' +
                ", containedObject=" + containedObject +
                '}';
    }
}