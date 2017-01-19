package tn.crns.ecos.outils;

import java.util.Date;
import java.util.Properties;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Session Bean implementation class EmailSessionBean
 */
@Stateless
@LocalBean
public class EmailSessionBean {

    /**
     * Default constructor. 
     */
    public EmailSessionBean() {
    }
    
    private String port = "465";
    private String host = "smtp.gmail.com";
    private String from = "affesabir@gmail.com";
    private boolean auth = true;
    private String username = "affesabir@gmail.com";
    private String password = "affesabiraffesabir";
    private Protocol protocol = Protocol.SMTPS;
    private boolean debug = true;
    
    @SuppressWarnings("incomplete-switch")
	public void sendEmail (String to,String subject,String body){
    	
    	Properties props = new Properties();
    	props.put("mail.smtp.host", host);
    	props.put("mail.smtp.port", port);
    	switch (protocol) {
    	    case SMTPS:
    	        props.put("mail.smtp.ssl.enable", true);
    	        break;
    	    case TLS:
    	        props.put("mail.smtp.starttls.enable", true);
    	        break;
    	}
    	
    	
    	Authenticator authenticator = null;
    	if (auth) {
    	    props.put("mail.smtp.auth", true);
    	    authenticator = new Authenticator() {
    	        private PasswordAuthentication pa = new PasswordAuthentication(username, password);
    	        @Override
    	        public PasswordAuthentication getPasswordAuthentication() {
    	            return pa;
    	        }
    	    };
    	}
    	
    	Session session = Session.getInstance(props, authenticator);
    	session.setDebug(debug);
    	
    	
    	MimeMessage message = new MimeMessage(session);
    	try {
    	    message.setFrom(new InternetAddress(from));
    	    InternetAddress[] address = {new InternetAddress(to)};
    	    message.setRecipients(Message.RecipientType.TO, address);
    	    message.setSubject(subject);
    	    message.setSentDate(new Date());
    	    message.setText(body);
    	    Transport.send(message);

    	} catch (MessagingException ex) {
    	    ex.printStackTrace();
    	}
    	
    }
    
    public void sendEmail2(){
    	 Properties props = new Properties();
         props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.starttls.enable", "true");
         props.put("mail.smtp.host", "smtp.gmail.com");
         props.put("mail.smtp.port", "587");

         Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
             protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
             }
           });

         try {

             Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress("affesabir@gmail.com"));
             message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse("affesabir@gmail.com"));
             message.setSubject("Testing Subject");
             message.setText("Dear Mail Crawler,"
                 + "\n\n No spam to my email, please!");

             Transport.send(message);

             System.out.println("Done");

         } catch (MessagingException e) {
             throw new RuntimeException(e);
         }
    }
    

}
