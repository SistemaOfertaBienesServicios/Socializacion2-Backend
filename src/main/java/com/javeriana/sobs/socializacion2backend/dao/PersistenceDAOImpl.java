package com.javeriana.sobs.socializacion2backend.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.javeriana.sobs.socializacion2backend.model.Role;

@Component
public class PersistenceDAOImpl implements PersistenceDAO {

	private DatabaseProperties dataPropierties = new DatabaseProperties();

	@Override
	public List<Role> getRoles() throws SQLException{
		String urlPostgresConnection = dataPropierties.getPropValues("postgresUrl");
		String userPostgresConnection = dataPropierties.getPropValues("postgresUsername");
		String passwordPostgresConnection = dataPropierties.getPropValues("postgresPassword");
		List<Role> allRoles = new ArrayList<>();
		Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sobs.role;");
        while (resultSet.next()) {
        	Role resultRole = new Role(resultSet.getString("role_id"), resultSet.getString("role_name"));
        	allRoles.add(resultRole);
        }
		return allRoles;
	}

}
