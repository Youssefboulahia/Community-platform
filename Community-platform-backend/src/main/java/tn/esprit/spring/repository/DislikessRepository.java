package tn.esprit.spring.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tn.esprit.spring.entities.Dislikess;
import tn.esprit.spring.entities.Post;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.UserAuth;

public interface DislikessRepository extends JpaRepository<Dislikess, Long>{
	Dislikess findByPostAndUser(Post post, UserAuth user);
	
	@Query("SELECT d.user.id, COUNT(d.user) FROM Dislikess AS d WHERE date >= :startDate AND date <= :endDate GROUP BY d.user ORDER BY COUNT(d.user) DESC")
	List<Object[]> countTotalDislikesByMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
