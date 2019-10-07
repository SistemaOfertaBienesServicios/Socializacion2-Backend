package com.javeriana.sobs.socializacion2backend.model.wrapper;

/**
 *
 * @author Carlos Ramirez
 *
 */
public class ResponseWrapper {
	
	private Object data;

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResponseWrapper [data=" + data + "]";
	}
	
	
}
