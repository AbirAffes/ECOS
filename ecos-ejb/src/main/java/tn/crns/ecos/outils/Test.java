package tn.crns.ecos.outils;

import java.io.File;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import tn.crns.ecos.model.Station;


public class Test {

	public static void main(String[] args) {
		
		//EmailSessionBean sender =new EmailSessionBean();
		//sender.sendEmail("affesabir@gmail.com", "essai", "test");
		
		//sender.sendEmail2();
		/*final String username = "affesabir@gmail.com";
        final String password = "affesabiraffesabir";
		Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username,password);
                }
            });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("affesabir@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("affesabir@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler," +
                    "\n\n No spam to my email, please!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
*/
		/*EmailClass email=new EmailClass();
		email.sendEmail("affesabir@gmail.com", "test", "ok");*/
		
		/*
		Station s= new Station();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Station.class);


	    Marshaller marshaller = context.createMarshaller() ;
	    marshaller.setProperty("jaxb.encoding",  "UTF-8") ;
	    marshaller.setProperty("jaxb.formatted.output", true) ;
	    marshaller.marshal(s,  new File("tableau.xml")) ;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Station st= new Station();
		StationPDF s=new StationPDF();
		s.downloadPDF(st);
	}

}
