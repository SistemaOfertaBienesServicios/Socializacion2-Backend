/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeriana.sobs.socializacion2backend.controller;

import java.util.List;

/**
 *
 * @author cristianmendi
 */
class WrapperExternalEndp {
    List<ProductEndpWrapper> products;

    public WrapperExternalEndp() {
    }

    public WrapperExternalEndp(List<ProductEndpWrapper> products) {
        this.products = products;
    }

    public List<ProductEndpWrapper> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEndpWrapper> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "WrapperExternalEndp{" + "products=" + products + '}';
    }

}
