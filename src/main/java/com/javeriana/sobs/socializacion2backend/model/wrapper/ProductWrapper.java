/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeriana.sobs.socializacion2backend.model.wrapper;

/**
 *
 * @author cristianmendi
 */
public class ProductWrapper {
    private String name;
    private long quantity;

    public ProductWrapper() {
    }

    public ProductWrapper(String name, long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductWrapper{" + "name=" + name + ", quantity=" + quantity + '}';
    }
    
    
}
