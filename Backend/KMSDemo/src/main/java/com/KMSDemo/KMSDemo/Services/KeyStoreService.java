package com.KMSDemo.KMSDemo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KMSDemo.KMSDemo.Entities.KeyStore;
import com.KMSDemo.KMSDemo.Exceptions.KeysNotFound;
import com.KMSDemo.KMSDemo.Repositories.KeyStoreRepo;

@Service
public class KeyStoreService {
	
	@Autowired
	private KeyStoreRepo keyStoreRepo;
	
	
	public KeyStore save(KeyStore keystore) {
		return keyStoreRepo.save(keystore);
		
	}
	
	public KeyStore getKeyStore(int id) {
		return keyStoreRepo.findById(id).orElseThrow(()->new KeysNotFound("Keys not found on server"));
	}
	
	public KeyStore getKeyStoreUsingEncryptedDEK(String encryptedDEK) {
		return keyStoreRepo.findByEncryptedDEK(encryptedDEK);
	}

}
