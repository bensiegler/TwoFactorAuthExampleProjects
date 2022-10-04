package com.bensiegler.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.twofa.dtos.SignInAttempt;
import org.springframework.security.web.authentication.twofa.stategies.sendattemp.TwoFactorAuthCodeSendStrategy;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class AwsEmailSendStrategy implements TwoFactorAuthCodeSendStrategy {

	private final Session session;

	private final String protocol = "smtp";
	private final String port = "587";
	private final boolean smtpTLS = true;
	private final boolean smtpAuth = true;
	private final String sendFromAddress = "bensiegler@bensiegler.com";
	private final String sendFromName = "Ben Siegler";
	private final String host = "email-smtp.eu-west-1.amazonaws.com";
	private final String username = "";
	private final String password = "";

	public AwsEmailSendStrategy() {
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", protocol);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.starttls.enable", smtpTLS);
		props.put("mail.smtp.auth", smtpAuth);

		session = Session.getDefaultInstance(props);
	}

	public Message generateEmailContent(String code, String emailAddress) throws MessagingException {
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sendFromAddress, sendFromName));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
			msg.setSubject("Your Requested Two Factor Authentication Code");
			msg.setContent(String.join(
					System.getProperty("line.separator"),
					"<h2>Here is your 2FA Code</h2>",
					"<p>" + code + "</p>"
			), "text/html");

			return msg;
		}catch (UnsupportedEncodingException e) {
			throw new MessagingException("the character encoding for given email addresses were invalid");
		}
	}

	@Override
	public void sendCode(UserDetails userDetails, SignInAttempt attempt) throws Exception {
		sendEmail(generateEmailContent(attempt.getTwoFactorCode(), userDetails.getTwoFactorAuthPreferences().get(1).getData()));
	}

	public void sendEmail(Message message) throws MessagingException {
		Thread t = new Thread(() -> {
			Transport transport;
			try {
				transport = session.getTransport();
				transport.connect(host, username, password);
				transport.sendMessage(message, message.getAllRecipients());
			} catch (MessagingException e) {
				throw new RuntimeException("something went wrong sending the code", e.getCause());
			}
		});

		try {
			t.start();
		}catch (RuntimeException e) {
			throw new MessagingException(e.getMessage());
		}
	}

}
