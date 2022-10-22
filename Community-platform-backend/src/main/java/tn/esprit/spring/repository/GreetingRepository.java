package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.spring.entities.Comment;
import tn.esprit.spring.entities.Greeting;

public interface GreetingRepository extends JpaRepository<Greeting, Long>{

}
