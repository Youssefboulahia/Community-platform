package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.spring.entities.Profile;

public interface ProfileRepository extends JpaRepository <Profile, Long> {

}
