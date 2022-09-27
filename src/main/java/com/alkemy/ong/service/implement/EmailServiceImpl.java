package com.alkemy.ong.service.implement;

import java.io.IOException;

import com.alkemy.ong.security.dto.RegisterDTO;
import org.springframework.stereotype.Service;

import com.alkemy.ong.dto.OrganizationDTO;
import com.alkemy.ong.service.EmailService;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Service
public class EmailServiceImpl implements EmailService {
	
	private String emailSender = System.getenv("EMAIL_SENDER");
	
	@Override
	public void sendWelcomeEmailTo(RegisterDTO user) {
		String apiKey = System.getenv("EMAIL_API_KEY");
		
		OrganizationDTO org = new OrganizationDTO();
		
        Mail mail = new Mail();
        mail.setFrom(new Email(emailSender, org.getWelcomeText()));
        mail.setTemplateId("d-0095599c09174d18861a8ff9ff062617");

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("first_name", user.getFirstName());
        personalization.addDynamicTemplateData("ong_name", org.getName());
        personalization.addDynamicTemplateData("ong_mail", org.getEmail());
        personalization.addDynamicTemplateData("ong_phone", org.getPhone());
        personalization.setSubject("Registro realizado con exito");

        personalization.addTo(new Email(user.getEmail()));

        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try{
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        }
        catch (IOException e){
            throw new RuntimeException("The mail couldn't be sent");
        }
		
	}

	@Override
	public void sendEmailTo(String toArg) {
		Email from = new Email(System.getenv("EMAIL_SENDER"));
	    String subject = "Contacto ONG - Somos Mas";
	    Email to = new Email(toArg);
	    Content content = new Content("text/plain", "Estamos felices de contactarnos. En breve alguien del equipo se comunicara contigo");
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid(System.getenv("EMAIL_API_KEY"));
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	      System.out.println(response.getStatusCode());
	      System.out.println(response.getBody());
	      System.out.println(response.getHeaders());
	    } catch (IOException e) {
	      throw new RuntimeException("The mail couldn't be sent");
	    }
	  }

}
