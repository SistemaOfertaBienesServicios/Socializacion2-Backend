package com.javeriana.sobs.socializacion2backend.exception;

public class SocializacionException extends Exception {

	private static final long serialVersionUID = 1L;

	private SocializacionErrorCode errorCode;

	public SocializacionException() {
        super();
    }

	public SocializacionException(SocializacionErrorCode errorCode) {
        this.errorCode = errorCode;
    }

	public SocializacionException(String message, SocializacionErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

	public SocializacionException(String message, Throwable cause, SocializacionErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

	public SocializacionException(Throwable cause, SocializacionErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

	public SocializacionErrorCode getErrorCode() {
		return errorCode;
	}
}