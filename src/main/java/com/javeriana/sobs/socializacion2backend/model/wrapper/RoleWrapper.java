package com.javeriana.sobs.socializacion2backend.model.wrapper;

public class RoleWrapper {
	
	private String token;
	private long id;

	public RoleWrapper() {
		super();
	}

	public RoleWrapper(String token) {
		super();
		this.token = token;
	}
	
	public RoleWrapper(String token, long id) {
		super();
		this.token = token;
		this.id = id;
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

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RoleWrapper [token=" + token + ", id=" + id + "]";
	}
	
}
