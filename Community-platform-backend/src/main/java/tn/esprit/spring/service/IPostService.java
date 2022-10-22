package tn.esprit.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import tn.esprit.spring.entities.Post;
import tn.esprit.spring.entities.User;

public interface IPostService {

	public List<Post> retrieveAllPost();
	
	public Post addPost(Post p, Long idUser, MultipartFile file);
	
	public void deletePost(Long idPost, Long idUser);
	
	public Post updatePost(Post p, Long id, Long idUser,  MultipartFile file);
	
	public Post updatePostNoPic(Post p, Long id, Long idUser);
	
	public Post retrievePostById(Long idPost);
	
	public String likePost(Long idPost, long idUser);
	
	public String dislikePost(Long idPost, long idUser);
	
	public Boolean checkLike(Long idPost, long idUser);
	
	public Boolean checkDislike(Long idPost, long idUser);
	
	public List<Object>  employeeOfTheMonth();
	
	public List<Post> filterPost(String str);
	
	public List<Post> findByUser_Id(Long id);
}
