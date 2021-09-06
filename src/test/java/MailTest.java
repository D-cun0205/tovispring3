import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailTest {

    public void naverMailSend() {
        //IMAP, SMTP의 차이가 궁금하다
        final String host = "smtp.naver.com";
        final String user = "twossic@naver.com";
        final String password = "N6208559025@#$";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", true);

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(user, password);
           }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("mmeo0205@gmail.com"));
            message.setSubject("This is Subject.");
            message.setText("This is Contents");

            Transport.send(message);
            System.out.println("Success Message Send");
        } catch(MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MailTest mt = new MailTest();
        mt.naverMailSend();
    }
}
