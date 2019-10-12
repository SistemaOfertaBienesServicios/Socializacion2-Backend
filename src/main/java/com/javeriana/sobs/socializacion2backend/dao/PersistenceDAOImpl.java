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
        long providerId = generatelongUUIDIdentifier();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO sobs.Provider (id, name, system) values (?, ?, ?)");
        stmt.setLong(1, providerId);
        stmt.setString(2, newProvider.getName());
        stmt.setBoolean(3, newProvider.isSystem());
        stmt.executeUpdate();
        connection.close();
        if (newProvider.isSystem()) {
            registerEndpoint(newProvider.getEndpoint(), providerId);
        }
        return newProvider;
    }

    public void registerEndpoint(EndpointInfo endpoint, long providerId) throws SQLException {
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement stmtep = connection.prepareStatement("INSERT INTO sobs.EndPointInfo (id, endpoint, endpointParameters,provider_id) values (?, ?, ?, ?)");
        stmtep.setLong(1, providerId);
        stmtep.setString(2, endpoint.getEndpoint());
        stmtep.setString(3, endpoint.getEndpointParameters());
        stmtep.setLong(4, providerId);
        stmtep.executeUpdate();
        connection.close();
    }

    public List<Product> getProductsByProvider(long provider_id) throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        String consulta = "SELECT * FROM sobs.product Where provider_id = ? ";
        PreparedStatement statement = connection.prepareStatement(consulta);
        statement.setLong(1, provider_id);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Product p = new Product(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getLong("price"), resultSet.getLong("quantity"));
            products.add(p);
        }
        connection.close();
        return products;
    }

    @Override
    public List<Quotation> getQuotations(long providerId) throws SQLException {
        List<Quotation> quotations = new ArrayList<>();
        String consulta = "SELECT * FROM sobs.quotation Where provider_id = ? ";
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement statement = connection.prepareStatement(consulta);
        statement.setLong(1, providerId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            List<Product> products = getQuotationProducts(resultSet.getLong("id"));
            Quotation quotation = new Quotation(resultSet.getLong("id"), resultSet.getLong("total"), products, resultSet.getString("user_username"),resultSet.getLong("provider_id"));
            quotations.add(quotation);
        }
        connection.close();
        return quotations;
    }

    public List<Product> getQuotationProducts(long quotationId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String consulta = "SELECT product_id,quantity FROM sobs.product_quotation Where Quotation_id = ? ";
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement statement = connection.prepareStatement(consulta);
        statement.setLong(1, quotationId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            long product_id = resultSet.getLong("product_id");
            long quantity = resultSet.getLong("quantity");
            Product product = getProductbyId(product_id, quantity);
            products.add(product);
        }
        connection.close();
        return products;
    }

    public Product getProductbyId(long product_id, long quantity) throws SQLException {

        Product product= new Product();
        String consulta = "SELECT * FROM sobs.product Where id = ? ";
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement statement = connection.prepareStatement(consulta);
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

    @Override
    public List<Provider> getProviders() throws SQLException {
        List<Provider> providers = new ArrayList<>();
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sobs.Provider;");
        while (resultSet.next()) {
            Provider provider = new Provider(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getBoolean("system"));
            provider.setProducts(getProductsByProvider(resultSet.getLong("id")));
            provider.setEndpoint(getEndpoint(provider.getId()));
            providers.add(provider);
        }
        connection.close();
        return providers;
    }

    public EndpointInfo getEndpoint(long provider_id) throws SQLException {
        EndpointInfo endpoint = new EndpointInfo();
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);

        String consulta = "SELECT * FROM sobs.EndPointInfo where provider_id=?";
        PreparedStatement statement = connection.prepareStatement(consulta);
        statement.setLong(1, provider_id);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            endpoint = new EndpointInfo(resultSet.getLong("id"), resultSet.getString("endpoint"), resultSet.getString("endpointParameters"));
        }
        connection.close();
        return endpoint;
    }
    
    public Provider getProviderByName(String name) {
    	Provider provider = null;
    	try {
    		Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM sobs.Provider WHERE name=?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
            	provider = new Provider();
            	provider.setId(resultSet.getLong("id"));
            	provider.setSystem(resultSet.getBoolean("system"));
            	provider.setName(resultSet.getString("name"));
            }
            connection.close();
            return provider;
    	} catch (Exception e) {
    		System.out.println(e.toString());
    	}
    	return provider;
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
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setEmail(resultSet.getString("email"));
            String role = resultSet.getString("role");
            if (role.contains("Proveedor")) {
            	PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM sobs.Provider WHERE name=?");
                statement2.setString(1, resultSet.getString("username"));
                ResultSet resultSet2 = statement2.executeQuery();
            	while (resultSet2.next()) {
            		user.setId(resultSet2.getLong("id"));
                }
            }
        }
        connection.close();
        return user;

    }

    private long generatelongUUIDIdentifier() {
    	return (new java.util.Random().nextLong() % (999999L - -999999L)) + -999999L;
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
	public boolean updateOrCreateProviderProducts(long providerId, List<Product> products)  throws SQLException {
		Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
		for(Product product : products) {
			PreparedStatement statementConsult = connection.prepareStatement("SELECT * FROM sobs.Product WHERE name=? AND Provider_id=?");
			statementConsult.setString(1, product.getName());
			statementConsult.setLong(2, providerId);
	        ResultSet resultSet = statementConsult.executeQuery();
	        
	        boolean validateExistingProduct = false;
	        while (resultSet.next()) {
	        	validateExistingProduct = true;
	        	PreparedStatement statementUpdate = connection.prepareStatement("UPDATE sobs.Product SET price=?, quantity=? WHERE name=? AND Provider_id=?");
	        	statementUpdate.setLong(1, product.getPrice());
	        	statementUpdate.setLong(2, product.getQuantity());
	        	statementUpdate.setString(3, product.getName());
	        	statementUpdate.setLong(4, providerId);
	        	statementUpdate.execute();
	        }
	       
	        if(!validateExistingProduct) {
	        	PreparedStatement statementUpdate = connection.prepareStatement("INSERT INTO sobs.Product (id, name, price, quantity, Provider_id) VALUES (?, ?, ?, ?, ?)");
	        	statementUpdate.setLong(1, generatelongUUIDIdentifier());
	        	statementUpdate.setString(2, product.getName());
	        	statementUpdate.setLong(3, product.getPrice());
	        	statementUpdate.setLong(4, product.getQuantity());
	        	statementUpdate.setLong(5, providerId);
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
        long quotationId = generatelongUUIDIdentifier();
        stmtep.setLong(1, quotationId);
        stmtep.setLong(2, newQuotation.getTotal());
        stmtep.setLong(3, newQuotation.getProviderId());
        stmtep.setString(4, newQuotation.getUsername());
        stmtep.executeUpdate();
        connection.close();
        List<Product> prods = newQuotation.getProducts();
        for (Product product : prods) {
            long prodId=generatelongUUIDIdentifier();
            product.setId(prodId);
            saveProduct(product.getId(), product.getName(), product.getPrice(), product.getQuantity(), newQuotation.getProviderId());
            saveProductQuotation((int) product.getQuantity(), quotationId, product.getId());
        }
        connection.close();
        return newQuotation;
    }

    public void saveProductQuotation(int quantity, long quotation_id, long product_id) throws SQLException {
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement stmtep = connection.prepareStatement("INSERT INTO sobs.product_quotation (quantity, quotation_id, product_id) values (?, ?, ?)");
        long quotationId = generatelongUUIDIdentifier();
        stmtep.setInt(1, quantity);
        stmtep.setLong(2, quotation_id);
        stmtep.setLong(3, product_id);
        stmtep.executeUpdate();
        connection.close();
    }

    public void saveProduct(long id, String name, long price, long quantity, long provider_id) throws SQLException {
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement stmtep = connection.prepareStatement("INSERT INTO sobs.product (id, name, price,quantity,provider_id) values (?, ?, ?, ?,?)");
        long quotationId = generatelongUUIDIdentifier();
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
            if (product.indexOf(productName) != -1) {
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
            if (!checkRepeatedName(productName, productsName)) {
                Product product = new Product();
                product.setId(Long.parseLong(resultSet.getString("id")));
                product.setName(resultSet.getString("name"));
                product.setPrice(Long.parseLong(resultSet.getString("price")));
                product.setQuantity(Long.parseLong(resultSet.getString("quantity")));
                products.add(product);
                productsName.add(productName);
            }
        }
        connection.close();
        return products;
    }

    @Override
    public User getUser(String usename) throws SQLException {
        User user= new User();
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM sobs.User WHERE username=?");
        statement.setString(1, usename);
        ResultSet resultSet = statement.executeQuery();
        Long id = 0L;
        while (resultSet.next()) {
        	user.setEmail(resultSet.getString("email"));
                user.setUsername(resultSet.getString("username"));
        }
        connection.close();
        return user;
    }
    
    public Product getProductByProvAndname(long providerId, String productName) throws SQLException {
        Product product= new Product();
        Connection connection = DriverManager.getConnection(urlPostgresConnection, userPostgresConnection, passwordPostgresConnection);
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM sobs.Product WHERE name=? and provider_id=?");
        statement.setString(1, productName);
        statement.setLong(2, providerId);
        ResultSet resultSet = statement.executeQuery();
        Long id = 0L;
        while (resultSet.next()) {
            product.setId(resultSet.getLong("id"));
            product.setPrice(resultSet.getLong("price"));
            product.setName(productName);
            product.setQuantity(resultSet.getLong("quantity"));
        }
        connection.close();
        return product;
    }

    @Override
    public List<Product> getProductsInfo(List<Product> products, long provider_Id) throws SQLException {
        List<Product> productsInfo = new ArrayList<>();
        for (Product product: products){
            Product tempProd= getProductByProvAndname(provider_Id,product.getName());
            productsInfo.add(tempProd);
        }
        return productsInfo;
    }
    

    

}
