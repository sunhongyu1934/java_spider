package tianyancha.XinxiXin;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class send_mail {

    public static void main(String args[]) throws UnsupportedEncodingException, MessagingException {
        send_mail.send("<p>当前账号：</p>"+"<p>11111111111</p>"+"     需要验证，请帮忙验证<p>注：此邮件为程序自动发送邮件，不需回复</p>");
    }

    public static void send(String html) throws MessagingException, UnsupportedEncodingException {
        Properties prop=new Properties();
        prop.setProperty("mail.host","smtp.mxhichina.com");
        prop.setProperty("mail.transport.protocol","smtp");
        prop.setProperty("mail.smtp.auth","true");
        prop.setProperty("mail.smtp.port", "465");
        prop.setProperty("mail.smtp.socketFactory.port", "465");
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session= Session.getInstance(prop);
        session.setDebug(true);
        Transport transport=session.getTransport("smtp");
        transport.connect("smtp.mxhichina.com","hongyu.sun@innotree.cn","ShyInnotree*");
        Message message = createMimeMessage(session,html);
        //5、发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public static MimeMessage createMimeMessage(Session session,String context) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage=new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress("hongyu.sun@innotree.cn","spider","utf-8"));
        mimeMessage.setRecipients(Message.RecipientType.TO, "ren.lan@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "enzhen.xiao@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "haixu.jin@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "weigong.wu@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "kaixiang.chen@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "peng.yi@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "shaohang.yuan@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "ling.fu@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "jianan.zhang@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "xiaoyan.yang@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "min.cheng@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "xiaogui.liu@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "jiakang.sun@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "hongyu.sun@innotree.cn");

        mimeMessage.setSubject("天眼查需验证账号","utf-8");
        //邮件的文本内容
        mimeMessage.setContent(context, "text/html;charset=UTF-8");
        return mimeMessage;
    }
}
