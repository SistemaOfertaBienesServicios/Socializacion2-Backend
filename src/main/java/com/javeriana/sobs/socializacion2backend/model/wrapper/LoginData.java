package com.javeriana.sobs.socializacion2backend.model.wrapper;

/**
 * 
 * @author Carlos Ramirez
 *
 */
public class LoginData {
	
	private String username;
	private String password;
	
	public LoginData() {
		super();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginData [username=" + username + ", password=" + password + "]";
	}
	
}
