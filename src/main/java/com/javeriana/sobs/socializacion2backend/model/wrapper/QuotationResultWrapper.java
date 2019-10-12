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
public class QuotationResultWrapper {
    private long total;

    public QuotationResultWrapper() {
    }

    public QuotationResultWrapper(long total) {
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "QuotationResultWrapper{" + "total=" + total + '}';
    }
    
}
