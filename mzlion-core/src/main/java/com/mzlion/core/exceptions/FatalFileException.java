package com.mzlion.core.exceptions;

/**
 * Thrown on an unrecoverable problem encountered in the file operate.
 *
 * @author mzlion
 */
public class FatalFileException extends RuntimeException {

    public FatalFileException(String message) {
        super(message);
    }

    public FatalFileException(Throwable cause) {
        super(cause);
    }

    public FatalFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
