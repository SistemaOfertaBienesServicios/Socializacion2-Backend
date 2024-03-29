package com.javeriana.sobs.socializacion2backend.dao;

import com.javeriana.sobs.socializacion2backend.model.Product;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import com.javeriana.sobs.socializacion2backend.model.Quotation;
import java.sql.SQLException;
import java.util.List;

import com.javeriana.sobs.socializacion2backend.model.Role;
import com.javeriana.sobs.socializacion2backend.model.User;

public interface PersistenceDAO {
    public List<Role> getRoles() throws SQLException;
    public Provider registerProvider(Provider newProvider) throws SQLException;
    public boolean validateToken(String token) throws SQLException;
    public List<Provider> getProviders() throws SQLException;
    public User validateLoginUser(String username, String password) throws SQLException;
    public boolean updateOrCreateProviderProducts(long providerId, List<Product> products)  throws SQLException;
    public Quotation saveQuotation(Quotation newQuotation) throws SQLException;
    public List<Quotation> getQuotations(long providerId) throws SQLException;
    public List<Product> getProducts() throws SQLException;
    public long consultIdFromProvider(String name) throws SQLException;
    public User getUser(String usename) throws SQLException;


}
