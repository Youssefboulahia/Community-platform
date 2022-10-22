package tn.esprit.spring.controllers;

import java.time.LocalDate;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import tn.esprit.spring.entities.Post;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.repository.DislikessRepository;
import tn.esprit.spring.repository.LikessRepository;
import tn.esprit.spring.repository.PostRepository;
import tn.esprit.spring.service.IPostService;

@RestController 
@RequestMapping("/post") 
@CrossOrigin
public class PostController {
	
	@Autowired
	PostRepository postRepo;
	@Autowired
	LikessRepository likeRepo;
	@Autowired
	DislikessRepository dislikeRepo;
	
	@Autowired
	IPostService postService;
	
	
	@GetMapping("/retrieveAll") 
	@ResponseBody 
	public List<Post> retrieveAllPost() {
		return postService.retrieveAllPost();
	}
	
	@GetMapping("/employeeOfTheMonth") 
	@ResponseBody 
	public List<Object> employeeOfTheMonth() {
		return postService.employeeOfTheMonth();
	}
	
	@PostMapping("/filter") 
	public List<Post> filter(@RequestBody String str) {
		return postService.filterPost(str);
	}
	
	@GetMapping("/postMonth") 
	@ResponseBody 
	public List<Object[]> postMonth() {
		Date dateStart = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date dateEnd = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		return postRepo.countTotalPostsByMonth(dateStart, dateEnd);
	}
	
	@GetMapping("/likesMonth") 
	@ResponseBody 
	public List<Object[]> likesMonth() {
		Date dateStart = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date dateEnd = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		return likeRepo.countTotalLikesByMonth(dateStart, dateEnd);
	}
	
	@GetMapping("/dislikesMonth") 
	@ResponseBody 
	public List<Object[]> dislikesMonth() {
		Date dateStart = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date dateEnd = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		return dislikeRepo.countTotalDislikesByMonth(dateStart, dateEnd);
	}

	//consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE  }
	@PostMapping(path="/add/{idUser}", consumes = {"multipart/form-data", MediaType.APPLICATION_JSON_VALUE }) 
	@ResponseBody 
	public Post addPost(@PathVariable Long idUser, @RequestPart("imageFile") MultipartFile file , @RequestPart("subject") String subject,@RequestPart("content") String content) {
		Post p = new Post();
		p.setSubject(subject);
		p.setContent(content);
		return postService.addPost(p, idUser,file);
		
	}

	@PostMapping("/delete/{idPost}/{idUser}") 
	public void deletePost(@PathVariable Long idPost,@PathVariable Long idUser) {
		postService.deletePost(idPost, idUser);
		
	}

	@PostMapping(path="/update/{idPost}/{idUser}", consumes = {"multipart/form-data", MediaType.APPLICATION_JSON_VALUE }) 
	@ResponseBody 
	public Post updateMessage( @PathVariable Long idPost, @PathVariable Long idUser, @RequestPart("imageFile") MultipartFile file, @RequestPart("subject") String subject,@RequestPart("content") String content) {
		Post p = postService.retrievePostById(idPost);
		p.setSubject(subject);
		p.setContent(content);
		return postService.updatePost(p, idPost, idUser, file);
	}
	
	@PostMapping(path="/updateNoPic/{idPost}/{idUser}") 
	@ResponseBody 
	public Post updateNoPic( @PathVariable Long idPost, @PathVariable Long idUser,  @RequestPart("subject") String subject,@RequestPart("content") String content) {
		Post p = postService.retrievePostById(idPost);
		p.setSubject(subject);
		p.setContent(content);
		return postService.updatePostNoPic(p, idPost, idUser);
	}

	@GetMapping("/retrieveBy/{idPost}") 
	@ResponseBody 
	public Post retrievePostById(@PathVariable Long idPost) {
		 return postService.retrievePostById(idPost);
	}
	
	@GetMapping("/retrieveByUser/{idUser}") 
	@ResponseBody 
	public List<Post> retrieveByUser(@PathVariable Long idUser) {
		 return postService.findByUser_Id(idUser);
	}
	
	@PostMapping("/like/{idPost}/{idUser}") 
	@ResponseBody 
	public String likePost(@PathVariable Long idPost, @PathVariable Long idUser) {
		 return postService.likePost(idPost, idUser);
	}
	
	@PostMapping("/dislike/{idPost}/{idUser}") 
	@ResponseBody 
	public String dislikePost(@PathVariable Long idPost, @PathVariable Long idUser) {
		 return postService.dislikePost(idPost, idUser);
	}
	
	@GetMapping("/checkLike/{idPost}/{idUser}") 
	@ResponseBody 
	public Boolean checkLike(@PathVariable Long idPost, @PathVariable Long idUser) {
		 return postService.checkLike(idPost, idUser);
	}
	
	@GetMapping("/checkDislike/{idPost}/{idUser}") 
	@ResponseBody 
	public Boolean checkDislike(@PathVariable Long idPost, @PathVariable Long idUser) {
		 return postService.checkDislike(idPost, idUser);
	}

}
