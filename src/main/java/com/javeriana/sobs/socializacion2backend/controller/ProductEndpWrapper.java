/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeriana.sobs.socializacion2backend.controller;

/**
 *
 * @author cristianmendi
 */
class ProductEndpWrapper {
    private String producto;
    private String cantidad;

    public ProductEndpWrapper() {
    }

    public ProductEndpWrapper(String producto, String cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantifad(String cantifad) {
        this.cantidad = cantifad;
    }

    @Override
    public String toString() {
        return "ProductEndpWrapper{" + "producto=" + producto + ", cantifad=" + cantidad + '}';
    }
    
    
}
