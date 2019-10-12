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
import com.javeriana.sobs.socializacion2backend.model.Quotation;
import com.javeriana.sobs.socializacion2backend.model.Role;
import com.javeriana.sobs.socializacion2backend.model.wrapper.LoginData;
import com.javeriana.sobs.socializacion2backend.model.wrapper.NewProviderWrapper;
import com.javeriana.sobs.socializacion2backend.model.wrapper.ProductWrapper;
import com.javeriana.sobs.socializacion2backend.model.wrapper.QuotationResultWrapper;
import com.javeriana.sobs.socializacion2backend.model.wrapper.QuotationsWrapper;
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
            if (roleWrapperResponse == null) {
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
    
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> getProducts() throws SocializacionException {
        try {
        	List<Product> products = socializacionServiceImpl.getProducts();
            return new ResponseEntity<>(generateResponseWrapper(products), HttpStatus.OK);
        } catch (Exception ex) {
            throw handleException(ex);
        }
    }

    @RequestMapping(path = "/register/{token}", method = RequestMethod.POST)
    public ResponseEntity<?> registerProvider(@RequestBody NewProviderWrapper newProvider, @PathVariable("token") String token) {
        try {
            boolean auth = socializacionServiceImpl.validateToken(token);
            if (auth) {
                Provider newProv = new Provider();
                newProv.setName(newProvider.getName());
                newProv.setSystem(newProvider.isSystem());
                newProv.setEndpoint(newProvider.getEndpoint());
                socializacionServiceImpl.registerProvider(newProv);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLException.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/quote", method = RequestMethod.POST)
    public ResponseEntity<?> makeQuotes(@RequestBody QuotationsWrapper quotationData)  {
        try {
            List<Quotation> quotations = socializacionServiceImpl.makeQuotes(quotationData.getProducts(),quotationData.getUsername());
            return new ResponseEntity<>(quotations,HttpStatus.CREATED);
        } catch (SQLException ex) {
            Logger.getLogger(SocializacionController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(path = "/saveQuote", method = RequestMethod.POST)
    public ResponseEntity<?> saveQuotes(@RequestBody Quotation quotation)  {
        try {
            Quotation newQuotation = socializacionServiceImpl.saveQuotation(quotation);
            return new ResponseEntity<>(newQuotation,HttpStatus.CREATED);
        } catch (SQLException ex) {
            Logger.getLogger(SocializacionController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    
    @RequestMapping(value = "/quotations/{providerId}", method = RequestMethod.GET)
    public ResponseEntity<List<Quotation>> getQuotations(@PathVariable ("providerId") long providerId) throws SocializacionException {
        try {
            List<Quotation> quotations = socializacionServiceImpl.getQuotations(providerId);
            return new ResponseEntity<>(quotations, HttpStatus.OK);
        } catch (Exception ex) {
            throw handleException(ex);
        }
    }
    
    @RequestMapping(path = "/pruebaEndp", method = RequestMethod.POST)
    public ResponseEntity<?> pruebaEndp(@RequestBody List<ProductWrapper> products)  {
        System.out.println("pruebaEndp");
        for(ProductWrapper pw : products){
            System.out.println(pw.toString());
        }
        QuotationResultWrapper qrw=  new QuotationResultWrapper(190000);
        return new ResponseEntity<>(qrw, HttpStatus.OK);
    }
    
    @RequestMapping(path = "/pruebaEndp2", method = RequestMethod.POST)
    public ResponseEntity<?> pruebaEndp2(@RequestBody List<ProductWrapper> products)  {
        System.out.println("pruebaEndp2");
        for(ProductWrapper pw : products){
            System.out.println(pw.toString());
        }
        QuotationResultWrapper qrw=  new QuotationResultWrapper(190000);
        return new ResponseEntity<>(qrw, HttpStatus.OK);
    }

}
