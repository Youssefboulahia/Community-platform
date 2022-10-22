package tn.esprit.spring.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.spring.entities.Comment;
import tn.esprit.spring.entities.SubComment;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.UserAuth;
import tn.esprit.spring.repository.CommentRepository;
import tn.esprit.spring.repository.SubCommentRepository;
import tn.esprit.spring.repository.UserAuthRepository;
import tn.esprit.spring.repository.UserRepository;

@Service
public class SubCommentService implements ISubCommentService{
	
	@Autowired 
	CommentRepository commentRepository;
	@Autowired 
	SubCommentRepository subCommentRepository;
	@Autowired 
	UserAuthRepository userRepository;

	@Override
	public List<SubComment> retrieveAllSubComment() {
		return (List<SubComment>) subCommentRepository.findAll();
	}

	@Override
	public SubComment addSubComment(SubComment c, Long idSubComment, Long idUser) {
		Comment comment = commentRepository.getById(idSubComment);
		UserAuth user = userRepository.getById(idUser);
		c.setComment(comment);
		c.setUser(user);
		c.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		return subCommentRepository.save(c);
	}

	@Override
	public void deleteSubComment(Long idSubComment, Long idUser) {
		SubComment subComment = subCommentRepository.getById(idSubComment);
		if(subComment.getUser().getId()==idUser || subComment.getComment().getPost().getUser().getId()==idUser)
		{
		subCommentRepository.deleteById(idSubComment);
		}
		
	}

	@Override
	public SubComment updateSubComment(SubComment c, Long id, Long idUser) {
		SubComment subCommentMain = subCommentRepository.getById(id);
		if(subCommentMain.getUser().getId()==idUser || subCommentMain.getComment().getPost().getUser().getId()==idUser)
		{
		SubComment subComment = subCommentRepository.getById(id);
		subComment.setText(c.getText());
		
		return subCommentRepository.save(subComment);
		}else{
			return subCommentMain;
		}
	}

	@Override
	public Optional<SubComment> retrieveSubCommentById(Long idSubComment) {
		return subCommentRepository.findById(idSubComment);
	}
	
	public List<Object[]> subCommentMonth() {
		Date dateStart = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date dateEnd = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		return subCommentRepository.countTotalSubCommentsByMonth(dateStart, dateEnd);
	}

}
