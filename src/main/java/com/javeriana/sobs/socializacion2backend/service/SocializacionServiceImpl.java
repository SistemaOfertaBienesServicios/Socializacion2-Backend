package com.javeriana.sobs.socializacion2backend.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javeriana.sobs.socializacion2backend.dao.PersistenceDAO;
import com.javeriana.sobs.socializacion2backend.exception.SocializacionErrorCode;
import com.javeriana.sobs.socializacion2backend.exception.SocializacionException;
import com.javeriana.sobs.socializacion2backend.logic.QuotationsLogic;
import com.javeriana.sobs.socializacion2backend.mail.SocializacionMail;
import com.javeriana.sobs.socializacion2backend.model.Product;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import com.javeriana.sobs.socializacion2backend.model.Quotation;
import com.javeriana.sobs.socializacion2backend.model.Role;
import com.javeriana.sobs.socializacion2backend.model.User;
import com.javeriana.sobs.socializacion2backend.model.wrapper.QuotationWrapper;
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
    public User validateLoginUser(String username, String password) throws SQLException, SocializacionException {
        if (!username.isEmpty() && !password.isEmpty()) {
            User validateUser = persistenceDAOImpl.validateLoginUser(username, password);
            if (validateUser != null) {
                return validateUser;
            }
            return null;
        } else {
            throw new SocializacionException("Username or Password is empty", SocializacionErrorCode.INVALID_ARGUMENTS);
        }

    }

    @Override
    public StatusInfo updateOrCreateProviderProducts(long providerId, List<Product> products) throws SocializacionException, SQLException {
        if (!products.isEmpty()) {
            boolean response = persistenceDAOImpl.updateOrCreateProviderProducts(providerId, products);
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
    public List<Quotation> makeQuotes(List<Product> products, String username, String email) throws SQLException {
        List<Quotation> allQuotes = new ArrayList<>();
        List<List<Provider>> provs = quotlogic.getProvidersClassified(products);
        List<QuotationWrapper> localQuotes = quotlogic.createQuotations(provs.get(1), products, username, email);
        User user = getUser(username);
        quotlogic.sendExternalQuots(provs.get(0), products, user.getUsername(), user.getEmail());
        for (QuotationWrapper quote : localQuotes) {
            Quotation qu = new Quotation(quote.getTotal(), quote.getProducts(), quote.getUsername(), quote.getProviderId());
            saveQuotation(qu, user.getEmail(), quote.getProviderName(),false);
            System.out.println("pppp");
            System.out.println(qu);
            allQuotes.add(qu);
        }
        return allQuotes;
    }

    @Override
    public Quotation saveQuotation(Quotation quotation, String email, String nameProvider, boolean external) throws SQLException {
        System.out.println("quotation");
        System.out.println(quotation.toString());
        SocializacionMail.sendEmail(email, quotation, nameProvider, !external);
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

    @Override
    public User getUser(String usename) throws SQLException {
        return persistenceDAOImpl.getUser(usename);
    }

}
