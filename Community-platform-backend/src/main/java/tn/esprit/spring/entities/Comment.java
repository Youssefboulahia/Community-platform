package tn.esprit.spring.entities;

import java.io.Serializable;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Comment implements Serializable {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long idComment;
	private String text;
	@Temporal(TemporalType.DATE)
	private Date dateComment;
	

	@JsonIgnore
	@ManyToOne
    private Post post;
	
	@JsonManagedReference
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy="comment")
	private Set<SubComment> subComments ;
	
	
	@ManyToOne
    private UserAuth user;

}
