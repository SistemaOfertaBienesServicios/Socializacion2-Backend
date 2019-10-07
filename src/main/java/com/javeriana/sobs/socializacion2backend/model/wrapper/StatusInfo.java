package com.javeriana.sobs.socializacion2backend.model.wrapper;

public class StatusInfo {
	
	private String info;

	public StatusInfo() {
		super();
	}

	public StatusInfo(String info) {
		super();
		this.info = info;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "StatusInfo [info=" + info + "]";
	}
	
	
}
