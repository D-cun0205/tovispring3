import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

public class MailTest {

    String host, user, password, toUser, port;
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getToUser() { return toUser; }
    public void setToUser(String toUser) { this.toUser = toUser; }
    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }

    public MailTest(String host, String user, String password, String toUser, String port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.toUser = toUser;
        this.port = port;
    }

    public void sendMail() {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.port", port);
        //google의 경우 props 추가
        if("465".equals(port)) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.trust", host);
        }

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(user, password);
           }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));

            //수신자가 여러명의 경우(addRecipient : x, addRecipients : o)
            //InternetAddress[] internetAddressArray = new InternetAddress[x];
            //addArray[0] = new InternetAddress("toUser1@mail.com");
            //addArray[1] = new InternetAddress("toUser2@mail.com");
            //message.addRecipients(Message.RecipientType.TO, internetAddressArray);

            message.setSubject("This is Subject.");
            message.setText("This is Contents");

            Transport.send(message);
            System.out.println("Success Message Send");
        } catch(MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //네이버
        MailTest naverMt = new MailTest("smtp.naver.com", "fromUser@mail.com", "password", "toUser@mail.com", "587");
        //구글
        MailTest googleMt = new MailTest("smtp.gmail.com", "fromUser@mail.com", "password", "toUser@mail.com", "465");
    }
}
