package com.javeriana.sobs.socializacion2backend.service;

import com.javeriana.sobs.socializacion2backend.model.Product;
import com.javeriana.sobs.socializacion2backend.model.Provider;

import java.sql.SQLException;
import java.util.List;

import com.javeriana.sobs.socializacion2backend.exception.SocializacionException;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import com.javeriana.sobs.socializacion2backend.model.Quotation;
import com.javeriana.sobs.socializacion2backend.model.Role;
import com.javeriana.sobs.socializacion2backend.model.wrapper.RoleWrapper;

public interface SocializacionService {
    public List<Role> getRoles() throws SQLException;
    public Provider registerProvider(Provider newProvider) throws SQLException;
    public boolean validateToken(String token) throws SQLException;
    public List<Quotation> makeQuotes(List<Product> products,String username) throws SQLException;
    public RoleWrapper validateLoginUser(String username, String password) throws SQLException, SocializacionException;
    public Quotation saveQuotation(Quotation quotation) throws SQLException;
}
