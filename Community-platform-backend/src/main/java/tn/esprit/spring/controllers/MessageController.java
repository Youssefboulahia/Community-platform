package tn.esprit.spring.controllers;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import tn.esprit.spring.entities.Greeting;
import tn.esprit.spring.entities.HelloMessage;
import tn.esprit.spring.entities.Message;
import tn.esprit.spring.entities.Post;
import tn.esprit.spring.repository.GreetingRepository;
import tn.esprit.spring.repository.UserAuthRepository;
import tn.esprit.spring.service.IMessageService;

@RestController
public class MessageController {

	@Autowired
	GreetingRepository messageService;
	
	@Autowired
	UserAuthRepository userRepo;

	@MessageMapping("/hello/{id}")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message, @PathVariable @DestinationVariable int id) throws Exception {
		
        Greeting msg = new Greeting("" + HtmlUtils.htmlEscape(message.getName().split("uniqueusername")[0]) + "", Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()), "" + HtmlUtils.htmlEscape(message.getName().split("uniqueusername")[2]) + "");
        msg.setUser(userRepo.getById((long) id));
		messageService.save(msg);
        return new Greeting("" + HtmlUtils.htmlEscape(message.getName()) + "");
    }
	
	@CrossOrigin
	@GetMapping("/getMessages") 
	@ResponseBody 
	public List<Greeting> retrieveAllMessages() {
		return messageService.findAll();
	}
}