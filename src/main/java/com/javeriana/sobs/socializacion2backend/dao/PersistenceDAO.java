package com.javeriana.sobs.socializacion2backend.dao;

import com.javeriana.sobs.socializacion2backend.model.Product;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import java.sql.SQLException;
import java.util.List;

import com.javeriana.sobs.socializacion2backend.model.Role;
import com.javeriana.sobs.socializacion2backend.model.User;

public interface PersistenceDAO {
    public List<Role> getRoles() throws SQLException;
    public Provider registerProvider(Provider newProvider) throws SQLException;
    public boolean validateToken(String token) throws SQLException;
    
    public User validateLoginUser(String username, String password) throws SQLException;
    public boolean updateOrCreateProviderProducts(String provider, List<Product> products)  throws SQLException;
}
