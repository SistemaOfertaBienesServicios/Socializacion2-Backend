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
public class WrapperExternalBody {
    private List<Product> products ;

    public WrapperExternalBody() {
    }

    public WrapperExternalBody(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "WrapperExternalBody{" + "products=" + products + '}';
    }
    
    
}
