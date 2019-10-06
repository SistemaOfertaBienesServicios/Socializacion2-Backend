package com.javeriana.sobs.socializacion2backend.controller;

import java.sql.SQLException;

import com.javeriana.sobs.socializacion2backend.Exception.SocializacionErrorCode;
import com.javeriana.sobs.socializacion2backend.Exception.SocializacionException;

public abstract class BaseController {
	
	protected SocializacionException handleException(Exception exception) {
        if (exception instanceof IllegalArgumentException) {
            return new SocializacionException(exception.getMessage(), SocializacionErrorCode.BAD_REQUEST);
        } else if (exception instanceof SQLException) {
            return new SocializacionException("Unable to stablish DataBase Connection: " + exception.getMessage(), SocializacionErrorCode.BAD_DATABASE_CONNECTION);
        } else {
            return new SocializacionException(exception.getMessage(), SocializacionErrorCode.GENERAL);
        }
    }
}
