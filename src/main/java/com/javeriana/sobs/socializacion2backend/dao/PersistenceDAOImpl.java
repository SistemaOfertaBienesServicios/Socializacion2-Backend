package com.javeriana.sobs.socializacion2backend.dao;

import com.javeriana.sobs.socializacion2backend.model.EndpointInfo;
import com.javeriana.sobs.socializacion2backend.model.Product;
import com.javeriana.sobs.socializacion2backend.model.Provider;
import com.javeriana.sobs.socializacion2backend.model.Quotation;

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
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.util.UUID;
import javax.websocket.Endpoint;

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
        connection.close();
        return allRoles;
    }

    @Override
    public Provider registerProvider(Provider newProvider) throws SQLException {
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        long providerId = generateId();
        
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO sobs.Provider (id, name, system) values (?, ?, ?)");
        stmt.setLong(1, providerId);
        stmt.setString(2, newProvider.getName());
        stmt.setBoolean(3, newProvider.isSystem());
        stmt.executeUpdate();
        connection.close();
        if (newProvider.isSystem()){
            registerEndpoint(newProvider.getEndpoint(),providerId);
        }
        return newProvider;
    }
    
    public void registerEndpoint(EndpointInfo endpoint, long providerId) throws SQLException{
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement stmtep = connection.prepareStatement("INSERT INTO sobs.EndPointInfo (id, endpoint, endpointParameters,provider_id) values (?, ?, ?, ?)");
        stmtep.setLong(1, providerId);
        stmtep.setString(2, endpoint.getEndpoint());
        stmtep.setString(3, endpoint.getEndpointParameters());
        stmtep.setLong(4, providerId);
        stmtep.executeUpdate();
        connection.close();
    }
    
    @Override
    public List<Quotation> getQuotations(long providerId) throws SQLException {
        List<Quotation> quotations = new ArrayList<>();
        String consulta = "SELECT * FROM sobs.quotation Where providerId = ? ";
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement statement= connection.prepareStatement(consulta);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            List<Product> products = getQuotationProducts(resultSet.getLong("id"));
            Quotation quotation = new Quotation(resultSet.getLong("id"), resultSet.getLong("total"), products, resultSet.getString("username"),resultSet.getLong("providerId"));
            quotations.add(quotation);
        }
        connection.close();
        return quotations;
    }
    
    public List<Product> getQuotationProducts(long quotationId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String consulta = "SELECT product_id,quantity FROM sobs.product_quotation Where quotation_id = ? ";
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement statement= connection.prepareStatement(consulta);
        statement.setLong(1, quotationId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            long product_id = resultSet.getLong("product_id");
            long quantity = resultSet.getLong("quantity");
            Product product = getProductbyId(product_id,quantity);
            products.add(product);
        }
        connection.close();
        return products;
    }
    
    
    public Product getProductbyId(long product_id, long quantity) throws SQLException {
        Product product= new Product();
        String consulta = "SELECT * FROM sobs.product Where product_id = ? ";
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement statement= connection.prepareStatement(consulta);
        statement.setLong(1, product_id);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            product.setId(resultSet.getLong("id"));
            product.setName(resultSet.getString("name"));
            product.setPrice(resultSet.getLong("price"));
            product.setQuantity(quantity);
        }
        connection.close();
        return product;
    }

    @Override
    public boolean validateToken(String token) throws SQLException {
        return token.equals("rty678");
    }

    public List<Provider> getProviders() throws SQLException {
        List<Provider> allRoles = new ArrayList<>();
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sobs.Provider;");
        while (resultSet.next()) {
            Provider provider = new Provider(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getBoolean("system"));
            provider.setEndpoint(getEndpoint(provider.getId()));
            allRoles.add(provider);
        }
        connection.close();
        return allRoles;
    }

    public EndpointInfo getEndpoint(long id) throws SQLException {
        EndpointInfo endpoint = new EndpointInfo();
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sobs.EndPointInfo;");
        while (resultSet.next()) {
            endpoint = new EndpointInfo(resultSet.getLong("id"), resultSet.getString("endpoint"), resultSet.getString("endpointParameters"));
        }
        connection.close();
        return endpoint;
    }
    
    public long generateId() {
        final UUID uid = UUID.randomUUID();
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uid.getLeastSignificantBits());
        buffer.putLong(uid.getMostSignificantBits());
        final BigInteger bi = new BigInteger(buffer.array());
        long identifier = bi.longValue();
        return identifier;
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
        connection.close();
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
	
	@Override
	public long consultIdFromProvider(String name) throws SQLException {
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

    @Override
    public Quotation saveQuotation(Quotation newQuotation) throws SQLException {
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement stmtep = connection.prepareStatement("INSERT INTO sobs.Quotation (id, total, Provider_id,User_username) values (?, ?, ?, ?)");
        long quotationId = generateId();
        stmtep.setLong(1, quotationId);
        stmtep.setLong(2, newQuotation.getTotal());
        stmtep.setLong(3, newQuotation.getProviderId());
        stmtep.setString(4, newQuotation.getUsername());
        stmtep.executeUpdate();
        connection.close();
        List<Product> prods = newQuotation.getProducts();
        for(Product product: prods){
            saveProductQuotation((int) product.getQuantity() , newQuotation.getId(), product.getId());
            saveProduct(product.getId(), product.getName(), product.getPrice(), product.getQuantity(), newQuotation.getProviderId());        
        }     
        connection.close();
        return newQuotation;
    }

    public void saveProductQuotation(int quantity, long quotation_id, long product_id) throws SQLException {
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement stmtep = connection.prepareStatement("INSERT INTO sobs.product_quotation (quantity, quotation_id, product_id) values (?, ?, ?)");
        long quotationId = generateId();
        stmtep.setInt(1, quantity);
        stmtep.setLong(2, quotation_id);
        stmtep.setLong(3, product_id);
        stmtep.executeUpdate();
        connection.close();
    }
    
    public void saveProduct(long id, String name, long price, long quantity, long provider_id) throws SQLException {
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement stmtep = connection.prepareStatement("INSERT INTO sobs.product (id, name, price,quantity,provider_id) values (?, ?, ?, ?,?)");
        long quotationId = generateId();
        stmtep.setLong(1, id);
        stmtep.setString(2, name);
        stmtep.setLong(3, price);
        stmtep.setLong(4, quantity);
        stmtep.setLong(5, provider_id);
        stmtep.executeUpdate();
        connection.close();
    }
    
    private boolean checkRepeatedName(String productName, List<String> products) {
    	for (String product : products) {
			if(product.indexOf(productName) != -1) {
				return true;
			}
		}
    	return false;
    }

	@Override
	public List<Product> getProducts() throws SQLException {
		Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM sobs.Product");
        ResultSet resultSet = statement.executeQuery();
        List<Product> products = new ArrayList<>();
        List<String> productsName = new ArrayList<>();
        while (resultSet.next()) {
        	String productName = resultSet.getString("name");
        	if(!checkRepeatedName(productName, productsName)) {
        		Product product = new Product();
            	product.setId(Long.parseLong(resultSet.getString("id")));
            	product.setName(resultSet.getString("name"));
            	product.setPrice(Long.parseLong(resultSet.getString("price")));
            	product.setQuantity(Long.parseLong(resultSet.getString("quantity")));
            	products.add(product);
            	productsName.add(productName);
        	}
        }
        return products;
	}
    
    
}
