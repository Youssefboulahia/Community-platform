package tn.esprit.spring.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tn.esprit.spring.entities.Comment;
import tn.esprit.spring.entities.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c.user.id, COUNT(c.user) FROM Comment AS c WHERE dateComment >= :startDate AND dateComment <= :endDate GROUP BY c.user ORDER BY COUNT(c.user) DESC")
	List<Object[]> countTotalCommentsByMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	@Query("SELECT c, c.user FROM Comment AS c WHERE c.post.idPost = :id")
	List<Object[]> findByPostId(@Param("id") Long id);
}
