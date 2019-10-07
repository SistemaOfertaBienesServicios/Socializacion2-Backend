package com.javeriana.sobs.socializacion2backend.model.wrapper;

public class RoleWrapper {
	
	private String token;

	public RoleWrapper() {
		super();
	}

	public RoleWrapper(String token) {
		super();
		this.token = token;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}


	@Override
	public String toString() {
		return "RoleWrapper [token=" + token + "]";
	}
	
}
