package tn.esprit.spring.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Greeting {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long  idMsg;
	private String content;
	@Temporal(TemporalType.DATE)
	private Date dateMessage;
	private String room;
	
	@ManyToOne
    private UserAuth user;
	
	
	public Greeting(String content) {
		this.content=content;
	}
	
	public Greeting(String content, Date date) {
		this.content=content;
		this.dateMessage=date;
	}
	
	public Greeting(String content, Date date, String room) {
		this.content=content;
		this.dateMessage=date;
		this.room=room;
	}

    
    
}
