package com.javeriana.sobs.socializacion2backend.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javeriana.sobs.socializacion2backend.dao.PersistenceDAO;
import com.javeriana.sobs.socializacion2backend.exception.SocializacionErrorCode;
import com.javeriana.sobs.socializacion2backend.exception.SocializacionException;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import com.javeriana.sobs.socializacion2backend.model.Role;
import com.javeriana.sobs.socializacion2backend.model.User;
import com.javeriana.sobs.socializacion2backend.model.wrapper.RoleWrapper;

@Service
public class SocializacionServiceImpl implements SocializacionService {

    @Autowired
    private PersistenceDAO persistenceDAOImpl;

    @Override
    public List<Role> getRoles() throws SQLException {
        return persistenceDAOImpl.getRoles();
    }

    @Override
    public Provider registerProvider(Provider newProvider) throws SQLException{
        return persistenceDAOImpl.registerProvider(newProvider);
    }
    
    @Override
    public boolean validateToken(String token) throws SQLException{
        return persistenceDAOImpl.validateToken(token);
    }

	@Override
	public RoleWrapper validateLoginUser(String username, String password) throws SQLException, SocializacionException {
		if(!username.isEmpty() && !password.isEmpty()) {
			User validateUser = persistenceDAOImpl.validateLoginUser(username, password);
			if(validateUser!=null) {
				return new RoleWrapper(validateUser.getRole());
			}
			return null;
		} else {
			throw new SocializacionException("Username or Password is empty", SocializacionErrorCode.INVALID_ARGUMENTS);
		}
		
	}

}
