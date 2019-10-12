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
import java.util.ArrayList;

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
				if(validateUser.getRole().contains("Proveedor")) {
					Long id = persistenceDAOImpl.consultIdFromProvider(username);
					return new RoleWrapper(validateUser.getRole(), id);
				}
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
        List<Quotation> allQuotes = new ArrayList<>();
        List<List<Provider>> provs = quotlogic.getProvidersClassified();
        List<Quotation> localQuotes= quotlogic.createQuotations(provs.get(1),products,username);
        quotlogic.sendExternalQuots(provs.get(0),products,username);
        allQuotes.addAll(localQuotes);
        //allQuotes.addAll(externalQuotes);
        return allQuotes;
    }

    @Override
    public Quotation saveQuotation(Quotation quotation) throws SQLException {
        return persistenceDAOImpl.saveQuotation(quotation);
    }

    @Override
    public List<Quotation> getQuotations(long providerId) throws SQLException {
        return persistenceDAOImpl.getQuotations(providerId);
    }

	@Override
	public List<Product> getProducts() throws SQLException {
		return persistenceDAOImpl.getProducts();
	}

}
