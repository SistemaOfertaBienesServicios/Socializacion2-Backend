package com.javeriana.sobs.socializacion2backend.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javeriana.sobs.socializacion2backend.dao.PersistenceDAO;
import com.javeriana.sobs.socializacion2backend.model.Role;

@Service
public class SocializacionServiceImpl implements SocializacionService{

	@Autowired
	private PersistenceDAO persistenceDAOImpl;
	
	@Override
	public List<Role> getRoles() throws SQLException {
		return persistenceDAOImpl.getRoles();
	}

}
