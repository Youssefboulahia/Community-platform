package tn.esprit.spring.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tn.esprit.spring.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("SELECT p.user.id, COUNT(p.user) FROM Post AS p WHERE date >= :startDate AND date <= :endDate GROUP BY p.user ORDER BY COUNT(p.user) DESC")
	List<Object[]> countTotalPostsByMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	@Query("SELECT p FROM Post AS p WHERE p.content LIKE %:str%")
	List<Post> filterByContent(@Param("str") String str);
	
	@Query("SELECT p FROM Post AS p WHERE p.subject LIKE %:str%")
	List<Post> filterBySubject(@Param("str") String str);
	
	Optional<Post> findByName(String name);
	
	@Query("SELECT p FROM Post AS p WHERE p.user.id = :id")
	List<Post> findByUserId(@Param("id") Long id);
}
