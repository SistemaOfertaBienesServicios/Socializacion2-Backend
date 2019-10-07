package com.javeriana.sobs.socializacion2backend.dao;

import com.javeriana.sobs.socializacion2backend.model.Product;
import com.javeriana.sobs.socializacion2backend.model.Provider;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.javeriana.sobs.socializacion2backend.model.Role;
import com.javeriana.sobs.socializacion2backend.model.User;

@Component
public class PersistenceDAOImpl implements PersistenceDAO {

    private DatabaseProperties dataPropierties = new DatabaseProperties();

    private String urlPostgresConnection = dataPropierties.getPropValues("postgresUrl");
    private String userPostgresConnection = dataPropierties.getPropValues("postgresUsername");
    private String passwordPostgresConnection = dataPropierties.getPropValues("postgresPassword");
    
    @Override
    public List<Role> getRoles() throws SQLException {
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

    @Override
    public Provider registerProvider(Provider newProvider) throws SQLException{
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        System.out.println("registerProvider");
        return newProvider;
    }

    @Override
    public boolean validateToken(String token) throws SQLException {
        System.out.println("token: " + token);
        return token.equals("valido");
    }

	@Override
	public User validateLoginUser(String username, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM sobs.User WHERE username=? AND password=?");
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        User user = null;
        while (resultSet.next()) {
        	user = new User();
        	user.setRole(resultSet.getString("role"));
        }
        return user;
        
	}
	
	private long generatelongUUIDIdentifier() {
		final UUID uid = UUID.randomUUID();
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uid.getLeastSignificantBits());
        buffer.putLong(uid.getMostSignificantBits());
        final BigInteger bi = new BigInteger(buffer.array());
        return bi.longValue();
	}
	
	private long consultIdFromProvider(String name) throws SQLException {
		Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM sobs.Provider WHERE name=?");
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        Long id = 0L;
        while (resultSet.next()) {
        	id = Long.parseLong(resultSet.getString("id"));
        }
        connection.close();
        return id;
	}

	@Override
	public boolean updateOrCreateProviderProducts(String provider, List<Product> products)  throws SQLException {
		Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
		Long idProvider = consultIdFromProvider(provider);
		for(Product product : products) {
			PreparedStatement statementConsult = connection.prepareStatement("SELECT * FROM sobs.Product WHERE name=? AND Provider_id=?");
			statementConsult.setString(1, product.getName());
			statementConsult.setLong(2, idProvider);
	        ResultSet resultSet = statementConsult.executeQuery();
	        
	        boolean validateExistingProduct = false;
	        while (resultSet.next()) {
	        	validateExistingProduct = true;
	        	PreparedStatement statementUpdate = connection.prepareStatement("UPDATE sobs.Product SET price=?, quantity=? WHERE name=? AND Provider_id=?");
	        	statementUpdate.setLong(1, product.getPrice());
	        	statementUpdate.setLong(2, product.getQuantity());
	        	statementUpdate.setString(3, product.getName());
	        	statementUpdate.setLong(4, idProvider);
	        	statementUpdate.execute();
	        }
	       
	        if(!validateExistingProduct) {
	        	PreparedStatement statementUpdate = connection.prepareStatement("INSERT INTO sobs.Product (id, name, price, quantity, Provider_id) VALUES (?, ?, ?, ?, ?)");
	        	statementUpdate.setLong(1, generatelongUUIDIdentifier());
	        	statementUpdate.setString(2, product.getName());
	        	statementUpdate.setLong(3, product.getPrice());
	        	statementUpdate.setLong(4, product.getQuantity());
	        	statementUpdate.setLong(5, idProvider);
	        	statementUpdate.execute();
	        }
		}
		connection.close();
		return true;
	}
}
