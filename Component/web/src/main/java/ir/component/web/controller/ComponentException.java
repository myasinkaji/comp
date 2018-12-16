package ir.component.web.controller;

/**
 * @author Mohammad Yasin Kaji
 */
public class ComponentException extends RuntimeException {

    public ComponentException() {
        super();
    }

    public ComponentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentException(String message) {
        super(message);
    }

    public ComponentException(Throwable cause) {
        super(cause);
    }

}
