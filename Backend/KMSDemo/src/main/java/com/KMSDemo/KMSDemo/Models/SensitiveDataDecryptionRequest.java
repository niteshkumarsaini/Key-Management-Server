package com.KMSDemo.KMSDemo.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SensitiveDataDecryptionRequest {
	
	private String encryptedData;
	private String encryptedDEK;
	

}
