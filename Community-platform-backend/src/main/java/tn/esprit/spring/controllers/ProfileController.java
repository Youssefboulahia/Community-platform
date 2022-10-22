package tn.esprit.spring.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import tn.esprit.spring.entities.Post;
import tn.esprit.spring.entities.Profile;
import tn.esprit.spring.entities.UserAuth;
import tn.esprit.spring.repository.ProfileRepository;
import tn.esprit.spring.repository.UserAuthRepository;
import tn.esprit.spring.service.IPostService;

@RestController 
@RequestMapping("/profile") 
@CrossOrigin
public class ProfileController {

	@Autowired
	ProfileRepository profileRepo;
	@Autowired
	UserAuthRepository userRepo;
	
	//consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE  }
		@PostMapping(path="/update/{idUser}", consumes = {"multipart/form-data", MediaType.APPLICATION_JSON_VALUE }) 
		@ResponseBody 
		public Profile addProfile(@PathVariable Long idUser, @RequestPart("imageFile") MultipartFile file , @RequestPart("firstName") String firstName,@RequestPart("lastName") String lastName,@RequestPart("phoneNumber") String phoneNumber,@RequestPart("interests") String interests,@RequestPart("region") String region,@RequestPart("country") String country) {
			Profile profile = userRepo.getById(idUser).getProfile();
			profile.setFirstName(firstName);
			profile.setLastName(lastName);
			profile.setPhoneNumber(phoneNumber);
			profile.setCountry(country);
			profile.setRegion(region);
			profile.setInterests(interests);
			profile.setDateComment(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			
			
			profile.setName(file.getOriginalFilename());
			profile.setType(file.getContentType());
			try {
				profile.setPicByte(file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return profileRepo.save(profile);
			
		}
		
		@GetMapping("/retrieveBy/{id}") 
		@ResponseBody 
		public Profile retrievePostById(@PathVariable Long id) {
			UserAuth user = userRepo.getById(id);
			System.out.println("hey");
			System.out.println("hey");
			System.out.println("hey");
			System.out.println("hey");
			System.out.println("hey");
			
			 return user.getProfile();
		}
	
}
