/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeriana.sobs.socializacion2backend.model.wrapper;

import com.javeriana.sobs.socializacion2backend.model.EndpointInfo;

/**
 *
 * @author cristianmendi
 */
public class NewProviderWrapper {
    private String name;
    private boolean system;
    private EndpointInfo endpoint;

    public NewProviderWrapper(String name, boolean system, EndpointInfo endpoint) {
        this.name = name;
        this.system = system;
        this.endpoint = endpoint;
    }

    public NewProviderWrapper() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public EndpointInfo getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(EndpointInfo endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return "NewProviderWrapper{" + "name=" + name + ", system=" + system + ", endpoint=" + endpoint + '}';
    }
    
}
