package com.KMSDemo.KMSDemo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensitiveDataService {

	@Autowired
	private final KmsService kmsService;
	
	public SensitiveDataService(KmsService kmsService) {
		this.kmsService=kmsService;
		// TODO Auto-generated constructor stub
	}

	public String encryptSensitiveData(String data, String key) {
		//encrypt sensitive data
		try {
			String encryptedData=kmsService.encryptWithCustomKey(data, key);
			return encryptedData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

}
