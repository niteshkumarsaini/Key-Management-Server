package com.KMSDemo.KMSDemo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.KMSDemo.KMSDemo.Entities.KeyStore;

@Repository
public interface KeyStoreRepo extends JpaRepository<KeyStore,Integer> {
	
	public KeyStore findByEncryptedDEK(String encryptedDEK);

}
