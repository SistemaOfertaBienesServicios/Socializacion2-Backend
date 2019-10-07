package com.javeriana.sobs.socializacion2backend.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javeriana.sobs.socializacion2backend.dao.PersistenceDAO;
import com.javeriana.sobs.socializacion2backend.exception.SocializacionErrorCode;
import com.javeriana.sobs.socializacion2backend.exception.SocializacionException;
import com.javeriana.sobs.socializacion2backend.logic.QuotationsLogic;
import com.javeriana.sobs.socializacion2backend.model.Product;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import com.javeriana.sobs.socializacion2backend.model.Quotation;
import com.javeriana.sobs.socializacion2backend.model.Role;
import com.javeriana.sobs.socializacion2backend.model.User;
import com.javeriana.sobs.socializacion2backend.model.wrapper.RoleWrapper;
import com.javeriana.sobs.socializacion2backend.model.wrapper.StatusInfo;

@Service
public class SocializacionServiceImpl implements SocializacionService {

    @Autowired
    private PersistenceDAO persistenceDAOImpl;
    
    @Autowired
    private QuotationsLogic quotlogic;

	@Override
	public List<Role> getRoles() throws SQLException {
		return persistenceDAOImpl.getRoles();
	}

	@Override
	public RoleWrapper validateLoginUser(String username, String password) throws SQLException, SocializacionException {
		if (!username.isEmpty() && !password.isEmpty()) {
			User validateUser = persistenceDAOImpl.validateLoginUser(username, password);
			if (validateUser != null) {
				return new RoleWrapper(validateUser.getRole());
			}
			return null;
		} else {
			throw new SocializacionException("Username or Password is empty", SocializacionErrorCode.INVALID_ARGUMENTS);
		}

	}

	@Override
	public StatusInfo updateOrCreateProviderProducts(String username, List<Product> products) throws SocializacionException, SQLException{
		if (!username.isEmpty() && !products.isEmpty()) {
			boolean response = persistenceDAOImpl.updateOrCreateProviderProducts(username, products);
			if (!response) {
				throw new SocializacionException("Bad request processing!!", SocializacionErrorCode.BAD_REQUEST);
			}
			StatusInfo status = new StatusInfo("Validate Request");
			return status;
		} else {
			throw new SocializacionException("Username or Password is empty", SocializacionErrorCode.INVALID_ARGUMENTS);
		}
	}
	
    @Override
    public Provider registerProvider(Provider newProvider) throws SQLException {
        return persistenceDAOImpl.registerProvider(newProvider);
    }

    @Override
    public boolean validateToken(String token) throws SQLException {
        return persistenceDAOImpl.validateToken(token);
    }

    @Override
    public List<Quotation> makeQuotes(List<Product> products, String username) throws SQLException {
        List<Provider> provsWithoutS = quotlogic.getProvidersWithoutSystem();
        quotlogic.sendExternalQuots(products);
        List<Quotation> quotations= quotlogic.createQuotations(provsWithoutS,products,username);
        return quotations;
    }

    @Override
    public Quotation saveQuotation(Quotation quotation) throws SQLException {
        return persistenceDAOImpl.saveQuotation(quotation);
    }

}
