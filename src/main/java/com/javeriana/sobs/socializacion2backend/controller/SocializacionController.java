package com.javeriana.sobs.socializacion2backend.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javeriana.sobs.socializacion2backend.exception.SocializacionException;
import com.javeriana.sobs.socializacion2backend.model.Product;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import com.javeriana.sobs.socializacion2backend.model.Role;
import com.javeriana.sobs.socializacion2backend.model.wrapper.LoginData;
import com.javeriana.sobs.socializacion2backend.model.wrapper.ResponseWrapper;
import com.javeriana.sobs.socializacion2backend.model.wrapper.RoleWrapper;
import com.javeriana.sobs.socializacion2backend.model.wrapper.StatusInfo;
import com.javeriana.sobs.socializacion2backend.service.SocializacionService;

@Controller
public class SocializacionController extends BaseController {

    @Autowired
    SocializacionService socializacionServiceImpl;

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public ResponseEntity<List<Role>> getRoles() throws SocializacionException {
        try {
            return new ResponseEntity<>(socializacionServiceImpl.getRoles(), HttpStatus.OK);
        } catch (Exception ex) {
            throw handleException(ex);
        }
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> loginUser(@RequestBody LoginData loginData) throws SocializacionException {
        try {
        	RoleWrapper roleWrapperResponse = socializacionServiceImpl.validateLoginUser(loginData.getUsername(), loginData.getPassword());
        	if(roleWrapperResponse == null) {
        		return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        	}
            return new ResponseEntity<>(generateResponseWrapper(roleWrapperResponse), HttpStatus.OK);
        } catch (Exception ex) {
            throw handleException(ex);
        }
    }
    
    @RequestMapping(value = "/products/{providerId}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseWrapper> createProducts(@PathVariable("providerId") String providerId, @RequestBody List<Product> products) throws SocializacionException {
        try {
        	StatusInfo roleWrapperResponse = socializacionServiceImpl.updateOrCreateProviderProducts(providerId, products);
            return new ResponseEntity<>(generateResponseWrapper(roleWrapperResponse), HttpStatus.OK);
        } catch (Exception ex) {
            throw handleException(ex);
        }
    }
    

    @RequestMapping(path = "/register/{token}", method = RequestMethod.POST)
    public ResponseEntity<?> registerProvider(@RequestBody Provider newProvider, @PathVariable("token") String token) {
        try {
            //registrar dato
            System.out.println(newProvider);
            System.out.println(token);
            boolean auth = socializacionServiceImpl.validateToken(token);
            if (auth) {
                socializacionServiceImpl.registerProvider(newProvider);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLException.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
