/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeriana.sobs.socializacion2backend.logic;

import com.javeriana.sobs.socializacion2backend.dao.PersistenceDAO;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cristianmendi
 */
@Service
public class QuotationsLogic {
    
    @Autowired
    private PersistenceDAO persistenceDAOImpl;
    
    public List<Provider> getProvidersWithoutSystem(){
        List<Provider> allProviders = persistenceDAOImpl.getProviders();
        List<Provider> providersWithoutS = new ArrayList<>();
        allProviders.forEach((provider) -> {
            if(!provider.isSystem()){
                providersWithoutS.add(provider);
            }
        });
        return providersWithoutS;
    }
    
    public List<Provider> getProvidersWithSystem(){
        List<Provider> allProviders = persistenceDAOImpl.getProviders();
        List<Provider> providersWithS = new ArrayList<>();
        allProviders.forEach((provider) -> {
            if(provider.isSystem()){
                providersWithS.add(provider);
            }
        });
        return providersWithS;
    }
}
