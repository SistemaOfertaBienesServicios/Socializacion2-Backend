package com.javeriana.sobs.socializacion2backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javeriana.sobs.socializacion2backend.Exception.SocializacionException;
import com.javeriana.sobs.socializacion2backend.model.Role;
import com.javeriana.sobs.socializacion2backend.service.SocializacionService;

@Controller
public class SocializacionController extends BaseController{
	
	@Autowired
	SocializacionService socializacionServiceImpl;

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public ResponseEntity<List<Role>> getRoles() throws SocializacionException{
		try {
			return new ResponseEntity<>(socializacionServiceImpl.getRoles(), HttpStatus.OK);
		} catch (Exception ex) {
			throw handleException(ex);
		}
	}
}
