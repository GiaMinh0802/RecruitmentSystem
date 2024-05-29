package com.fpt.recruitmentsystem.service.implement;

import com.fpt.recruitmentsystem.dto.CandidateDTO;
import com.fpt.recruitmentsystem.dto.CollectedCandidateDTO;
import com.fpt.recruitmentsystem.model.Event;
import com.fpt.recruitmentsystem.model.Vacancy;
import com.fpt.recruitmentsystem.service.IMailService;
import com.fpt.recruitmentsystem.exception.BadRequestException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService implements IMailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendResetPasswordLink(String email, String link) {
        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Thank you!<p>"
                + "<p>Note: This link is set to expire in 5 minutes</p>";

        sendEmail(email, subject, content);
    }

    public void sendActiveAccountLink(String email, String link) {
        String subject = "Please verify your registration";
        String content = "<p>Hello,</p>"
                + "<p>Please click the link below to verify your registration:</p>"
                + "<p><a href=\"" + link + "\">Verify email</a></p>"
                + "<br>"
                + "<p>Thank you!<p>"
                + "<p>Note: This link is set to expire in 24 hours.</p>";

        sendEmail(email, subject, content);
    }

    public void sendApplyEventSuccess(Event event, CollectedCandidateDTO collectedCandidateDTO) {
        String subject = "Apply Success Event " + event.getName() + " (Recruitment System)";
        String content = "<p>Hello, " + collectedCandidateDTO.getFirstName() + " " + collectedCandidateDTO.getLastName() + "!</p>"
                + "<p>We are very pleased that you are interested in this event.</p>"
                + "<p>Here is some information about the event:</p>"
                + "<ul style=\"list-style-type: square\">"
                + "<li>Event name: \"" + event.getName() + "\"</li>"
                + "<li>Location: " + event.getLocation() + "</li>"
                + "<li>Time: " + event.getStartDate() + "</li>"
                + "</ul>"
                + "<p>I hope you will have a great experience in this event, your presence is our honor!</p>"
                + "<br />"
                + "<p>Thank you!</p>";
        sendEmail(collectedCandidateDTO.getEmail(), subject, content);
    }

    private void sendEmail(String email, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(emailFrom, "Recruitment System");
            helper.setTo(email);

            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BadRequestException("Error while send email");
        }
    }

    public void sendLoginGoogleSuccess(String email, String firstName, String lastName) {
        String subject = "Login Successfully (Recruitment System)";
        String content = "<p>Hello, " + firstName + " " + lastName + "!</p>"
                + "<p>Welcome to Recruitment System.</p>"
                + "<p>Thank you for joining with us.</p>"
                + "<p>We look forward to working with you.</p>"
                + "<br />"
                + "<p>Sincerely!</p>";
        sendEmail(email, subject, content);
    }

    @Override
    public void sendApplyVacancySuccess(Vacancy vacancy, CandidateDTO candidateDTO) {
        String subject = "Apply Success Vacancy " + vacancy.getPosition().getName() + " (Recruitment System)";
        String content = "<p>Hello, " + candidateDTO.getFirstName()+ " " + candidateDTO.getLastName() + "!</p>"
                + "<p>Thank you for applying for the job.</p>"
                + "<p>Check your email regularly for our interview invitations.</p>"
                + "<p>We look forward to working with you.</p>"
                + "<br />"
                + "<p>Thank you!</p>";
        sendEmail(candidateDTO.getEmail(), subject, content);
    }

}
