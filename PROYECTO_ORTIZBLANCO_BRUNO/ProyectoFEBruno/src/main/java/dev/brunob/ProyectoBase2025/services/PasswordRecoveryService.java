package dev.brunob.ProyectoBase2025.services;

import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.repositorios.UserRepository;

@Service
public class PasswordRecoveryService {

    private static final String PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789@#$%";
    private static final int PASSWORD_LENGTH = 12;

    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private UserRepository userRepository;

    @Value("${app.mail.username:}")
    private String usuarioDe;

    @Value("${app.mail.password:}")
    private String contrasenaUsuarioDe;

    @Value("${app.mail.smtp.starttls.enable:true}")
    private String envioSeguroTLS;

    @Value("${app.mail.smtp.auth:true}")
    private String autenticacionUsuarioDe;

    @Value("${app.mail.smtp.host:smtp.gmail.com}")
    private String servidorSMTP;

    @Value("${app.mail.smtp.port:587}")
    private String puertoServidorSMTP;

    @Transactional
    public void resetPasswordAndSendEmail(String email) {
        String normalizedEmail = email == null ? "" : email.trim();
        User user = userRepository.findByEmail(normalizedEmail);

        if (user == null) {
            throw new IllegalArgumentException("No existe ningun usuario registrado con ese email.");
        }

        String newPassword = generatePassword();
        user.setPassword(newPassword);
        userRepository.save(user);

        sendRecoveryEmail(user, newPassword);
    }

    private String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = secureRandom.nextInt(PASSWORD_CHARS.length());
            password.append(PASSWORD_CHARS.charAt(index));
        }
        return password.toString();
    }

    private void sendRecoveryEmail(User user, String newPassword) {
        enviarCorreo(usuarioDe,
                contrasenaUsuarioDe,
                envioSeguroTLS,
                autenticacionUsuarioDe,
                servidorSMTP,
                puertoServidorSMTP,
                user.getEmail(),
                "Nueva contraseña - Sistema de Gestión FE",
                buildEmailBody(user, newPassword));
    }

    private void enviarCorreo(String usuarioDe,
            String contrasenaUsuarioDe,
            String envioSeguroTLS,
            String autenticacionUsuarioDe,
            String servidorSMTP,
            String puertoServidorSMTP,
            String usuarioA,
            String asunto,
            String cuerpo) {

        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.starttls.enable", envioSeguroTLS);
        propiedades.put("mail.smtp.auth", autenticacionUsuarioDe);
        propiedades.put("mail.smtp.host", servidorSMTP);
        propiedades.put("mail.smtp.port", puertoServidorSMTP);

        Authenticator autenticador = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuarioDe, contrasenaUsuarioDe);
            }
        };

        Session sesion = Session.getInstance(propiedades, autenticador);

        try {
            Message mensaje = new MimeMessage(sesion);
            InternetAddress iaDe = new InternetAddress(usuarioDe);
            mensaje.setFrom(iaDe);
            InternetAddress[] iaA = InternetAddress.parse(usuarioA);
            mensaje.setRecipients(Message.RecipientType.TO, iaA);
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            Transport.send(mensaje);
        } catch (MessagingException e) {
            throw new IllegalStateException("Fallo en el envio del correo electronico: " + e.getMessage(), e);
        }
    }

    private String buildEmailBody(User user, String newPassword) {
        String name = user.getFirstName() != null && !user.getFirstName().trim().isEmpty()
                ? user.getFirstName().trim()
                : "usuario";

        return "Hola " + name + ",\n\n"
                + "Se ha solicitado restablecer la contraseña de tu cuenta en el Sistema de Gestión de Formaciones en Empresa.\n\n"
                + "Tu nueva contraseña es: " + newPassword + "\n\n"
                + "Puedes iniciar sesión con esta contraseña y cambiarla desde tu perfil si lo necesitas.\n\n"
                + "Si no solicitaste este cambio, contacta con administración.";
    }
}