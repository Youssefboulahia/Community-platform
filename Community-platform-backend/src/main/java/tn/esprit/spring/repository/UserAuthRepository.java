package tn.esprit.spring.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.UserAuth;
@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
	Optional<UserAuth> findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	
	
}
