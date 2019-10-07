/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeriana.sobs.socializacion2backend.model.wrapper;

import com.javeriana.sobs.socializacion2backend.model.Product;
import java.util.List;

/**
 *
 * @author cristianmendi
 */
public class QuotationsWrapper {

    private String username;
    private List<Product> products;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    
}
