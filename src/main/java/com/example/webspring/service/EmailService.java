// EmailService.java
package com.example.webspring.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${app.reset.password.url}")
    private String resetPasswordUrl;  // Ví dụ: https://yourapp.com/reset-password?token=

    @Async
    public void sendResetPasswordEmail(String toEmail, String token) {
        Email from = new Email("huynguyen1723@gmail.com");
        String subject = "Yêu cầu đặt lại mật khẩu";
        Email to = new Email(toEmail);
        String contentText = "Nhấn vào link sau để đặt lại mật khẩu của bạn: " + resetPasswordUrl + token;
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Email sent. Status Code: " + response.getStatusCode());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
