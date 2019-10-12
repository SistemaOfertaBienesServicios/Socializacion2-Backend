package com.javeriana.sobs.socializacion2backend.mail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.javeriana.sobs.socializacion2backend.model.Product;
import com.javeriana.sobs.socializacion2backend.model.Quotation;
import org.springframework.stereotype.Service;

public class SocializacionMail {
	private static String username = "wowpokemonwow@gmail.com";
	private static String password = "@Warlords29";
	
	private static String generateHTMLbody(Quotation quotation, String nameProvider) {
		String format = "<!DOCTYPE html> <html> <head> <style> table {   font-family: arial, sans-serif;   border-collapse: collapse;   width: 100%; }  td, th {   border: 1px solid #dddddd;   text-align: left;   padding: 8px; }  tr:nth-child(even) {   background-color: #dddddd; } </style> </head> <body>   <h1 style=\"color: #1D83AD;\">Cotización del proveedor " + nameProvider;
		format += "</h1> <p>Con base a la selección de los productos realizada por el cliente, se informará el detalle de la cotización realizada.</p>   <table>   <tr>     <th>Nombre</th>     <th>Precio (COP)</th>     <th>Cantidad</th>   </tr> ";
		for(Product product : quotation.getProducts()) {
			format += "<tr><td>"+product.getName()+"</td><td>"+product.getPrice()+"</td><td>"+product.getQuantity()+"</td></tr>";
		}
		format += "</table><p>El costo total de la cotización es de: $<span><strong>"+quotation.getTotal()+"</strong></span>.</p>   </body> </html> ";
		return format;
	}

	public static void sendEmail(String recipientEmail, Quotation quotation, String nameProvider) {
                String subjectEmail = "Corización proveedor: "+nameProvider;
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		String bodyEmail = generateHTMLbody(quotation, nameProvider);
		System.out.println(bodyEmail);
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("wowpokemonwow@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			message.setSubject(subjectEmail);
			message.setText(bodyEmail);
			message.setDataHandler(new DataHandler(new HTMLDataSource(bodyEmail)));
			session.getTransport("smtps");
			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	static class HTMLDataSource implements DataSource {

		private String html;

		public HTMLDataSource(String htmlString) {
			html = htmlString;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			if (html == null)
				throw new IOException("html message is null!");
			return new ByteArrayInputStream(html.getBytes());
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			throw new IOException("This DataHandler cannot write HTML");
		}

		@Override
		public String getContentType() {
			return "text/html";
		}

		@Override
		public String getName() {
			return "HTMLDataSource";
		}
	}
}
