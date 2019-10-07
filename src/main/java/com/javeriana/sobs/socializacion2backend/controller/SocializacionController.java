package com.javeriana.sobs.socializacion2backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javeriana.sobs.socializacion2backend.Exception.SocializacionException;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import com.javeriana.sobs.socializacion2backend.model.Role;
import com.javeriana.sobs.socializacion2backend.service.SocializacionService;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
