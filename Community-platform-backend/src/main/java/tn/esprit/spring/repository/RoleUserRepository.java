package tn.esprit.spring.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.ERole;
import tn.esprit.spring.entities.RoleUser;
@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
	Optional<RoleUser> findByName(ERole name);
}
