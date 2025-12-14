package Utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class EmailHandler extends Handler {
    private final String username = "diaachenko@gmail.com";
    private final String password = "xhjd pbim luqk pukd";
    private final String recipient = "diaachenko@gmail.com"; // Куди слати звіти

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel() != Level.SEVERE) {
            return;
        }

        sendEmail(record.getMessage(), record.getThrown());
    }

    private void sendEmail(String errorMsg, Throwable throwable) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("CRITICAL ERROR: GiftPack App");

            StringBuilder body = new StringBuilder();
            body.append("Помилка: ").append(errorMsg).append("\n\n");

            if (throwable != null) {
                body.append("Stack Trace:\n");
                for (StackTraceElement element : throwable.getStackTrace()) {
                    body.append(element.toString()).append("\n");
                }
            }

            message.setText(body.toString());
            Transport.send(message);

            System.err.println("[System]: Звіт про помилку надіслано на пошту.");

        } catch (MessagingException e) {
            System.err.println("[System]: Не вдалося надіслати email: " + e.getMessage());
        }
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}
}