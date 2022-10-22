package tn.esprit.spring.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tn.esprit.spring.entities.Dislikess;
import tn.esprit.spring.entities.Likess;
import tn.esprit.spring.entities.Post;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.UserAuth;
import tn.esprit.spring.repository.DislikessRepository;
import tn.esprit.spring.repository.LikessRepository;
import tn.esprit.spring.repository.PostRepository;
import tn.esprit.spring.repository.UserAuthRepository;
import tn.esprit.spring.repository.UserRepository;

@Service
public class PostService implements IPostService {
	
	@Autowired 
	PostRepository postRepository;
	
	@Autowired 
	UserAuthRepository userAuthRepository;
	
	@Autowired 
	LikessRepository likessRepository;
	@Autowired 
	DislikessRepository dislikessRepository;
	@Autowired 
	CommentService commentService;
	@Autowired 
	SubCommentService subCommentService;
	
	@Override
	public List<Post> retrieveAllPost() {
		 List<Post> l = postRepository.findAll();
		 for (Post p : l) {
			 p.setPicByte(p.getPicByte());
		 }
		 return l;
	}

	@Override
	public Post addPost(Post p, Long idUser, MultipartFile file) {
		UserAuth user = userAuthRepository.getById(idUser);
		p.setUser(user);
		p.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		p.setName(file.getOriginalFilename());
		p.setType(file.getContentType());
		try {
			p.setPicByte(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return postRepository.save(p);
	}

	@Override
	public void deletePost(Long idPost, Long idUser) {
		Post post = postRepository.getById(idPost);
		if(post.getUser().getId()==idUser)
		{
			postRepository.deleteById(idPost);
		}
		
		
	}

	@Override
	public Post updatePost(Post p, Long id, Long idUser,  MultipartFile file) {
		
	
		p.setName(file.getOriginalFilename());
		p.setType(file.getContentType());
		try {
			p.setPicByte(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return postRepository.save(p);
	}
	
	@Override
	public Post updatePostNoPic(Post p, Long id, Long idUser) {
		
		
		return postRepository.save(p);
	}
		
	

	@Override
	public Post retrievePostById(Long idPost) {
		Post postMain = postRepository.getById(idPost);
		postMain.setViews(postMain.getViews()+1);
		
		return postRepository.save(postMain);
	
	}
	
	@Override
	public List<Post> findByUser_Id(Long id)
	{
		return postRepository.findByUserId(id);
	}

	@Override
	public String likePost(Long idPost, long idUser) {
		UserAuth user = userAuthRepository.getById(idUser);
		Post post = postRepository.getById(idPost);
		Likess l = likessRepository.findByPostAndUser(post, user);
		Dislikess d = dislikessRepository.findByPostAndUser(post, user);
		
		if(l==null){
			if(d==null){
				//make like
				Likess newLike = new Likess();
				newLike.setPost(post);
				newLike.setUser(user);
				newLike.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				likessRepository.save(newLike);
				post.setLikes(post.getLikes()+1);
				postRepository.save(post);
				return("make like");
			}
			else{
				//remove dislike
				dislikessRepository.deleteById(d.getIdDislike());
				post.setDislikes(post.getDislikes()-1);
				postRepository.save(post);
				//make like
				Likess newLike = new Likess();
				newLike.setPost(post);
				newLike.setUser(user);
				newLike.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				likessRepository.save(newLike);
				post.setLikes(post.getLikes()+1);
				postRepository.save(post);
				return("remove dislike make like");
			}
			
		}else{
			//remove like
			likessRepository.deleteById(l.getIdLike());
			post.setLikes(post.getLikes()-1);
			postRepository.save(post);
			return("remove like");
		}
		
	}

	//dislike
	
	@Override
	public String dislikePost(Long idPost, long idUser) {
		UserAuth user = userAuthRepository.getById(idUser);
		Post post = postRepository.getById(idPost);
		Dislikess d = dislikessRepository.findByPostAndUser(post, user);
		Likess l = likessRepository.findByPostAndUser(post, user);
		
		if(d==null){
			if(l==null){
				//make dislike
				Dislikess newDislike = new Dislikess();
				newDislike.setPost(post);
				newDislike.setUser(user);
				newDislike.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				dislikessRepository.save(newDislike);
				post.setDislikes(post.getDislikes()+1);
				postRepository.save(post);
				return("make dislike");
			}
			else{
				//remove like
				likessRepository.deleteById(l.getIdLike());
				post.setLikes(post.getLikes()-1);
				postRepository.save(post);
				//make dislike
				Dislikess newDislike = new Dislikess();
				newDislike.setPost(post);
				newDislike.setUser(user);
				newDislike.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				dislikessRepository.save(newDislike);
				post.setDislikes(post.getDislikes()+1);
				postRepository.save(post);
				return("remove like make dislike");
			}
		}else{
			//remove dislike
			dislikessRepository.deleteById(d.getIdDislike());
			post.setDislikes(post.getDislikes()-1);
			postRepository.save(post);
			return("remove dislike");
		}
		
	}
	
	@Override
	public Boolean checkLike(Long idPost, long idUser) {
		UserAuth user = userAuthRepository.getById(idUser);
		Post post = postRepository.getById(idPost);
		Likess l = likessRepository.findByPostAndUser(post, user);
		Dislikess d = dislikessRepository.findByPostAndUser(post, user);
		
		if(l==null){
			return(false);}
		else{
			return(true);}
		}
	
	@Override
	public Boolean checkDislike(Long idPost, long idUser) {
		UserAuth user = userAuthRepository.getById(idUser);
		Post post = postRepository.getById(idPost);
		Likess l = likessRepository.findByPostAndUser(post, user);
		Dislikess d = dislikessRepository.findByPostAndUser(post, user);
		
		if(d==null){
			return(false);}
		else{
			return(true);}
		}
	
	
	public List<Post> filterPost(String str){
		
		List<Post> l1 = postRepository.filterByContent(str);
		List<Post> l2 = postRepository.filterBySubject(str);
		
		
		return l2;
	}
	
	//employee of the month
	
	public List<Object[]> postMonth() {
		Date dateStart = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date dateEnd = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		return postRepository.countTotalPostsByMonth(dateStart, dateEnd);
	}
	
	public List<Object[]> likesMonth() {
		Date dateStart = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date dateEnd = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		return likessRepository.countTotalLikesByMonth(dateStart, dateEnd);
	}
	
	public List<Object[]> dislikesMonth() {
		Date dateStart = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date dateEnd = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		return dislikessRepository.countTotalDislikesByMonth(dateStart, dateEnd);
	}
	
	//@Scheduled(cron = "0 0 0 1 * *")
	public List<Object> employeeOfTheMonth(){
		List<Object[]> postMonth = limitFiveWithScore(postMonth(), 10);
		List<Object[]> commentMonth = limitFiveWithScore(commentService.commentMonth(),8);
		List<Object[]> subCommentMonth = limitFiveWithScore(subCommentService.subCommentMonth(),6);
		List<Object[]> likeMonth = limitFiveWithScore(likesMonth(),4);
		List<Object[]> dislikeMonth = limitFiveWithScore(dislikesMonth(), 2);
		
		List<Object[]> l = new ArrayList<Object[]>();
		l=mergeScores(postMonth, commentMonth, subCommentMonth, likeMonth, dislikeMonth);
		
		long test = 0;
		List<Long> employeeOfTheMonth = new ArrayList<>();
		for(Object[] o:l){
			if(Long.valueOf(o[1].toString())>test){
				test =Long.valueOf(o[1].toString());
				employeeOfTheMonth.clear();
				employeeOfTheMonth.add(Long.valueOf(o[0].toString()));
				employeeOfTheMonth.add(Long.valueOf(o[1].toString()));
			}
		}
		List<Object> result = new ArrayList <Object>();
		result.add(employeeOfTheMonth.get(1));
		result.add(userAuthRepository.getById(employeeOfTheMonth.get(0)).getUsername());
		result.add(userAuthRepository.getById(employeeOfTheMonth.get(0)).getId());
		return result;
	}
	
	
	public List<Object[]> limitFiveWithScore(List<Object[]> l, int n){
		List<Object[]> list = new ArrayList<Object[]>();
		int i = 0;
		for(Object[] o:l){
			if(i<5){
				list.add(o);
				o[1]=Integer.valueOf(o[1].toString())*n;
				i++;
			}
			}
		return list;
	}
	
	public List<Object[]> mergeScores(List<Object[]> l1, List<Object[]> l2, List<Object[]> l3, List<Object[]> l4, List<Object[]> l5){
		List<Object[]> l = new ArrayList<Object[]>();
		l=l1;
		combineTwoLists(l2,l);
		combineTwoLists(l3,l);
		combineTwoLists(l4,l);
		combineTwoLists(l5,l);
		
		return l;
	}
	
	public void combineTwoLists(List<Object[]> list,List<Object[]> l){
		for(Object[] o:list){
			if(testEqual(l,Integer.valueOf(o[0].toString()))){
				for(Object[] ob:l){
					if(ob[0]==o[0]){
						ob[1]=Integer.valueOf(ob[1].toString())+Integer.valueOf(o[1].toString()) ;
					}
				}
			}else
			l.add(o);
		}
	}
	
	public boolean testEqual (List<Object[]> l, int obj){
		for(Object[] o:l){
			if(obj==Integer.valueOf(o[0].toString())){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	
	// compress the image bytes before storing it in the database
		public static byte[] compressBytes(byte[] data) {
			Deflater deflater = new Deflater();
			deflater.setInput(data);
			deflater.finish();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
			byte[] buffer = new byte[1024];
			while (!deflater.finished()) {
				int count = deflater.deflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			try {
				outputStream.close();
			} catch (IOException e) {
			}
			System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
			return outputStream.toByteArray();
		}
		// uncompress the image bytes before returning it to the angular application
		public static byte[] decompressBytes(byte[] data) {
			Inflater inflater = new Inflater();
			inflater.setInput(data);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
			byte[] buffer = new byte[1024];
			try {
				while (!inflater.finished()) {
					int count = inflater.inflate(buffer);
					outputStream.write(buffer, 0, count);
				}
				outputStream.close();
			} catch (IOException ioe) {
			} catch (DataFormatException e) {
			}
			return outputStream.toByteArray();
		}
	
	

}
