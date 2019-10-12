/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeriana.sobs.socializacion2backend.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javeriana.sobs.socializacion2backend.dao.DatabaseProperties;
import com.javeriana.sobs.socializacion2backend.dao.PersistenceDAO;
import com.javeriana.sobs.socializacion2backend.model.Product;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import com.javeriana.sobs.socializacion2backend.model.Quotation;
import com.javeriana.sobs.socializacion2backend.model.wrapper.QuotationWrapper;
import com.javeriana.sobs.socializacion2backend.model.wrapper.WrapperQuots;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cristianmendi
 */
@Service
public class QuotationsLogic {
    
    private ObjectMapper Obj = new ObjectMapper(); 
    private DatabaseProperties dataPropierties = new DatabaseProperties();
    private String sendQuotationsEndpoint = dataPropierties.getPropValues("sendQuotationsEndpoint");
    
    @Autowired
    private PersistenceDAO persistenceDAOImpl;
    
    public List<List<Provider>> getProvidersClassified() throws SQLException{
        List<List<Provider>> provs = new ArrayList<>();
        List<Provider> allProviders = persistenceDAOImpl.getProviders();
        List<Provider> providersWithoutS = new ArrayList<>();
        List<Provider> providersWithS = new ArrayList<>();
        allProviders.forEach((provider) -> {
            if(provider.isSystem()){
                providersWithS.add(provider);
            }
            else{
                providersWithoutS.add(provider);
            }
        });
        provs.add(providersWithS);
        provs.add(providersWithoutS);
        return provs;
    }

    public void sendExternalQuots(List<Provider> providers,List<Product> products, String username, String email) {
        WrapperQuots wrpQuo = new WrapperQuots(products,providers,username, email);
        String jsonStr;
        try {
            jsonStr = Obj.writeValueAsString(wrpQuo);
            String response = generatePOSTRequest(sendQuotationsEndpoint,jsonStr);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(QuotationsLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static String generatePOSTRequest(String endpoint, String body) {
        try {
            System.out.println("generatePOSTRequest");
            System.out.println(endpoint);
            System.out.println(body);
            URL obj = new URL(endpoint);
            HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("Content-Type", "application/json");
            postConnection.setDoOutput(true);
            OutputStream os = postConnection.getOutputStream();
            os.write(body.getBytes());
            os.flush();
            os.close();
            int responseCode = postConnection.getResponseCode();
            System.out.println("POST Response Code :  " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        postConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // print result
                return response.toString();
            } else {
                return null;
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(QuotationsLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QuotationsLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    public List<QuotationWrapper> createQuotations(List<Provider> providers, List<Product> products, String username, String email) throws SQLException {
        List<QuotationWrapper> quotations = new ArrayList<>();
        for(Provider prov : providers){
            List<Product> productsInfo = persistenceDAOImpl.getProductsInfo(products,prov.getId());
            QuotationWrapper quot = new QuotationWrapper();
            quot.setProducts(productsInfo);
            long total=0;
            for(int i=0; i<productsInfo.size();i++){
                productsInfo.get(i).setQuantity(products.get(i).getQuantity());
                total+=productsInfo.get(i).getPrice()*productsInfo.get(i).getQuantity();
            }
            quot.setTotal(total);
            quot.setProviderId(prov.getId());
            quot.setUsername(username);
            quot.setEmail(email);
            quot.setProviderName(prov.getName());
            quot.setProducts(productsInfo);
            System.out.println("quot");
            System.out.println(quot);
            quotations.add(quot);
            
        }
        
        return quotations;
    }
}
