package org.score.samples.openstack.actions;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



/**
 * Date: 8/19/2014
 *
 * @author lesant
 */


public class SimpleSendEmail {
	public static final String RETURN_CODE = "returnCode";
	public static final String SUCCESS = "0";
	public static final String FAILED = "1";

	final public Map<String, String> execute(String host, String port, String from, String to, String subject, String body){

		Map<String, String> returnResult = new HashMap<>();
		Session session;
		try {

			Properties props = System.getProperties();

			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);

			session = Session.getDefaultInstance(props);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);

			returnResult.put(RETURN_CODE, SUCCESS);

		} catch (Exception e) {
			returnResult.put(RETURN_CODE, FAILED);
		}

		return returnResult;

	}
}

