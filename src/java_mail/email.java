package java_mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by Administrator on 2017/6/7.
 */
public class email {

    public static void main(String args[]) throws MessagingException, UnsupportedEncodingException {
        Properties prop=new Properties();
        prop.setProperty("mail.host","smtp.mxhichina.com");
        prop.setProperty("mail.trabsport.protocol","smtp");
        prop.setProperty("mail.smtp.auth","true");

        Session session=Session.getInstance(prop);
        session.setDebug(true);
        Transport transport=session.getTransport();
        transport.connect("smtp.mxhichina.com","hongyu.sun@innotree.cn","3961Shy3961");
        Message message = createMimeMessage(session);
                //5、发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public static MimeMessage createMimeMessage(Session session) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage=new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress("hongyu.sun@innotree.cn"));
        mimeMessage.setRecipients(Message.RecipientType.TO, "hongyu.sun@innotree.cn");
        mimeMessage.setSubject("服务器配置信息","utf-8");
                 //邮件的文本内容
        mimeMessage.setContent("a024.hb2.innotree.org：", "text/html;charset=UTF-8");
        return mimeMessage;
    }
}
