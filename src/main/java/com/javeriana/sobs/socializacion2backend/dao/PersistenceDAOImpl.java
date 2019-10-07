package com.javeriana.sobs.socializacion2backend.dao;

import com.javeriana.sobs.socializacion2backend.model.EndpointInfo;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.javeriana.sobs.socializacion2backend.model.Role;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.util.UUID;
import javax.websocket.Endpoint;

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
        connection.close();
        return allRoles;
    }

    @Override
    public Provider registerProvider(Provider newProvider) throws SQLException{
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement stmtep = connection.prepareStatement("INSERT INTO sobs.EndPointInfo (id, endpoint, endpointParameters) values (?, ?, ?)");
        long providerId = generateId();
        stmtep.setLong(1,providerId);
        stmtep.setString(2, newProvider.getEndpoint().getEndpoint());
        stmtep.setString(3, newProvider.getEndpoint().getEndpointParameters());

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO sobs.Provider (id, name, system) values (?, ?, ?)");
        stmt.setLong(1,providerId);
        stmt.setString(2,newProvider.getName());
        stmt.setBoolean(3,newProvider.isSystem());
        stmt.executeUpdate();
        connection.close();
        return newProvider;
    }

    @Override
    public boolean validateToken(String token) throws SQLException {
        System.out.println("token: " + token);
        return token.equals("valido");
    }

    @Override
    public List<Provider> getProviders() throws SQLException{
        List<Provider> allRoles = new ArrayList<>();
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sobs.Provider;");
        while (resultSet.next()) {
            Provider provider = new Provider(resultSet.getLong("id"), resultSet.getString("name"),resultSet.getBoolean("system"));
            provider.setEndpoint(getEndpoint(provider.getId()));
            allRoles.add(provider);
        }
        connection.close();
        return allRoles;
    }
    
    public EndpointInfo getEndpoint(long id) throws SQLException{
        EndpointInfo endpoint= new EndpointInfo();
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sobs.EndPointInfo;");
        while (resultSet.next()) {
            endpoint = new EndpointInfo(resultSet.getLong("id"), resultSet.getString("endpoint"),resultSet.getString("endpointParameters"));
        }
        return endpoint;
    }
    
    public long generateId(){
        final UUID uid = UUID.randomUUID();
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uid.getLeastSignificantBits());
        buffer.putLong(uid.getMostSignificantBits());
        final BigInteger bi = new BigInteger(buffer.array());
        long identifier = bi.longValue();
        return identifier;
    }
}
