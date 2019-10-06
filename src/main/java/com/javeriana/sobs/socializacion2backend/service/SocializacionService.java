package com.javeriana.sobs.socializacion2backend.service;

import java.sql.SQLException;
import java.util.List;

import com.javeriana.sobs.socializacion2backend.model.Role;

public interface SocializacionService {
	public List<Role> getRoles() throws SQLException;
}
