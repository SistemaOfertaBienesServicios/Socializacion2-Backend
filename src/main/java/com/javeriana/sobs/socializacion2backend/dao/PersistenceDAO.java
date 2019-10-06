package com.javeriana.sobs.socializacion2backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.javeriana.sobs.socializacion2backend.model.Role;

public interface PersistenceDAO {
	public List<Role> getRoles() throws SQLException;
}
