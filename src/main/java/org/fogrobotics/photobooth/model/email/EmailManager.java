package org.fogrobotics.photobooth.model.email;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.fogrobotics.photobooth.model.BoothModel;
import org.fogrobotics.photobooth.model.customers.Customer;
import org.fogrobotics.photobooth.model.photo.Photo;

public class EmailManager
{
  private ArrayList<EmailMemoListener> memoListeners = new ArrayList<EmailMemoListener>();

  public EmailManager(BoothModel model)
  {
  }

  public void sendEmail(Customer c, Photo p)
  {
    Session session = getSession();
    try
    {
      MimeMessage msg = new MimeMessage(session);
      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
      msg.addHeader("format", "flowed");
      msg.addHeader("Content-Transfer-Encoding", "8bit");

      msg.setFrom(new InternetAddress("no_reply@fogrobotics.org", "NoReply-FOG-Robotics"));

      msg.setReplyTo(InternetAddress.parse("no_reply@fogrobotics.org", false));

      msg.setSubject("Team 7017 PhotoBooth - First Robotics 2018", "UTF-8");

      msg.setSentDate(new Date());

      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(c.getEmailAddress(), false));

      // Create the message body part
      BodyPart messageBodyPart = new MimeBodyPart();

      messageBodyPart.setText("We hoped you enjoyed using our photo booth.");

      // Create a multipart message for attachment
      Multipart multipart = new MimeMultipart();

      // Set text message part
      multipart.addBodyPart(messageBodyPart);

      // Second part is image attachment
      messageBodyPart = new MimeBodyPart();
      DataSource source = new FileDataSource(p.getArtiface());
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(p.getArtiface().getName());
      // Trick is to add the content-id header here
      messageBodyPart.setHeader("Content-ID", "image_id");
      multipart.addBodyPart(messageBodyPart);

      // third part for displaying image in the email body
      messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent("<h1>Attached Image</h1>" + "<img src='cid:image_id'>", "text/html");
      multipart.addBodyPart(messageBodyPart);

      // Set the multipart message to the email message
      msg.setContent(multipart);

      // Send message
      Transport.send(msg);
      setDisplayMemo("EMail Sent Successfully with image!!");
    }
    catch (MessagingException e)
    {
      e.printStackTrace();
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
  }

  private Session getSession()
  {
    final String fromEmail = "photo@bombholtmagic.com"; // requires valid
    final String password = "frogs1frogs"; // correct password for gmail id

    Properties props = new Properties();
    props.put("mail.smtp.host", "server335.serverquality.com"); // SMTP Host
    props.put("mail.smtp.port", "587"); // TLS Port
    props.put("mail.smtp.auth", "true"); // enable authentication
    props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

    Authenticator auth = new Authenticator()
    {
      protected PasswordAuthentication getPasswordAuthentication()
      {
        return new PasswordAuthentication(fromEmail, password);
      }
    };
    return Session.getInstance(props, auth);
  }

  public void setDisplayMemo(String memo)
  {
    for (EmailMemoListener lis : memoListeners)
    {
      lis.emailMemoChange(memo);
    }
  }

  public void addEmailMemoListener(EmailMemoListener lis)
  {
    memoListeners.add(lis);
  }

}
