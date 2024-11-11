/*package BankaYonetimSistemi;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailGonderici {
    public static void mailGonder(String alici, String konu, String icerik) {
        final String kullaniciAdi = "******";
        final String sifre = "*******";

        Properties ozellikler = new Properties();
        ozellikler.put("mail.smtp.auth", "true");
        ozellikler.put("mail.smtp.starttls.enable", "true");
        ozellikler.put("mail.smtp.host", "smtp.gmail.com");
        ozellikler.put("mail.smtp.port", "465");

        Session oturum = Session.getInstance(ozellikler,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(kullaniciAdi, sifre);
                    }
                });

        oturum.setDebug(true);

        try {
            Message mesaj = new MimeMessage(oturum);
            mesaj.setFrom(new InternetAddress(kullaniciAdi));
            mesaj.setRecipients(Message.RecipientType.TO, InternetAddress.parse(alici));
            mesaj.setSubject(konu);
            mesaj.setText(icerik);
            Transport.send(mesaj);
            System.out.println("Mail başarıyla gönderildi");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
*/
//Bu yeni gelecek bir özellik yakın zamanda koda entegre edilecektir