package com.javeriana.sobs.socializacion2backend.Exception;

public enum SocializacionErrorCode {
	GENERAL(1),
	INVALID_ARGUMENTS(2),
	BAD_DATABASE_CONNECTION(3),
	BAD_REQUEST(10);

	private int errorCode;

	SocializacionErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

	public int getErrorCode() {
		return errorCode;
	}
}
