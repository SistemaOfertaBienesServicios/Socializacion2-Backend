package com.javeriana.sobs.socializacion2backend.dao;

import com.javeriana.sobs.socializacion2backend.model.Provider;
import java.sql.SQLException;
import java.util.List;

import com.javeriana.sobs.socializacion2backend.model.Role;

public interface PersistenceDAO {
    public List<Role> getRoles() throws SQLException;
    public Provider registerProvider(Provider newProvider) throws SQLException;
    public boolean validateToken(String token) throws SQLException;

    public List<Provider> getProviders() throws SQLException;

}
