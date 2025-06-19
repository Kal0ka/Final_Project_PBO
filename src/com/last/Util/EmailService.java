package com.last.Util;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailService {
    private static final String FROM_EMAIL = "pascalkaloka79@gmail.com";
    private static final String FROM_PASSWORD = "exss cchn nwsq brib ";  // app password Gmail

    public static void sendRegistrationEmail(String toEmail, String nama) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Registrasi Berhasil - Sistem Perpustakaan");

            String content = "Halo " + nama + ",\n\n" +
                    "Akun Anda telah berhasil didaftarkan ke dalam Sistem Perpustakaan.\n" +
                    "Silakan login menggunakan email dan password Anda.\n\n" +
                    "Salam,\nAdmin Perpustakaan";

            message.setText(content);
            Transport.send(message);

            System.out.println("Notifikasi email berhasil dikirim ke " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
