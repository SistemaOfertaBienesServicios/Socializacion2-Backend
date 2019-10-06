/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeriana.sobs.socializacion2backend.model;

import java.util.List;

/**
 *
 * @author cristianmendi
 */
public class EndpointInfo {

	private long id;
	private String endpoint;
	private List<String> endpointParameters;
	

	public EndpointInfo() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public EndpointInfo(String endpoint, List<String> endpointParameters) {
		this.endpoint = endpoint;
		this.endpointParameters = endpointParameters;
	}

	/**
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * @param endpoint the endpoint to set
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * @return the endpointParameters
	 */
	public List<String> getEndpointParameters() {
		return endpointParameters;
	}

	/**
	 * @param endpointParameters the endpointParameters to set
	 */
	public void setEndpointParameters(List<String> endpointParameters) {
		this.endpointParameters = endpointParameters;
	}

	@Override
	public String toString() {
		return "EndpointInfo{" + "endpoint=" + endpoint + ", endpointParameters=" + endpointParameters + '}';
	}

}
