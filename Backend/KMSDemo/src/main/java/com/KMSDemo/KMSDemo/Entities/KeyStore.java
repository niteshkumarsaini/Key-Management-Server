package com.KMSDemo.KMSDemo.Entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeyStore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String encryptedDEK;
	private String hashkey;
	
	
	
	

}
