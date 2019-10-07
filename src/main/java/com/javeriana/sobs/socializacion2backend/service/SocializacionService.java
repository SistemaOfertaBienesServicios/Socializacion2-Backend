package com.javeriana.sobs.socializacion2backend.service;

import com.javeriana.sobs.socializacion2backend.model.Product;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import java.sql.SQLException;
import java.util.List;

import com.javeriana.sobs.socializacion2backend.model.Role;

public interface SocializacionService {
    public List<Role> getRoles() throws SQLException;
    public Provider registerProvider(Provider newProvider) throws SQLException;
    public boolean validateToken(String token) throws SQLException;
    public void makeQuotes(List<Product> products);
}
