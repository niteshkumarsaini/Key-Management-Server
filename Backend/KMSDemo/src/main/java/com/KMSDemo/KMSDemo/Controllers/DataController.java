package com.KMSDemo.KMSDemo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.KMSDemo.KMSDemo.Config.SecretsManagerService;
import com.KMSDemo.KMSDemo.Entities.KeyStore;
import com.KMSDemo.KMSDemo.Models.DataModel;
import com.KMSDemo.KMSDemo.Models.SensitiveDataDecryptionRequest;
import com.KMSDemo.KMSDemo.Models.SensitiveDataDecryptionResponse;
import com.KMSDemo.KMSDemo.Models.SensitiveDataEncryptionRequest;
import com.KMSDemo.KMSDemo.Models.SensitiveDataEncryptionResponse;
import com.KMSDemo.KMSDemo.Services.DataEncryptionService;
import com.KMSDemo.KMSDemo.Services.KeyStoreService;
import com.KMSDemo.KMSDemo.Services.KmsService;
import com.KMSDemo.KMSDemo.Util.Hashing;

@RestController
@RequestMapping("/api/data")
public class DataController {

	@Autowired
	private KmsService kmsService;
	
	@Autowired
	private KeyStoreService keyStoreService;
	private final DataEncryptionService dataEncryptionService;
	private final SecretsManagerService secretsManagerService;

	@Autowired
	public DataController(DataEncryptionService dataEncryptionService, SecretsManagerService secretsManagerService) {
		// TODO Auto-generated constructor stub
		this.dataEncryptionService = dataEncryptionService;
		this.secretsManagerService = secretsManagerService;
	}

	@PreAuthorize("hasRole('ROLE_SUPERADMIN')")
	@PostMapping("/encrypt")
	public ResponseEntity<?> encryptSensitiveData(@RequestBody SensitiveDataEncryptionRequest dataModel) {

		try {

			// Verify Encrypted DEK using Hash
			KeyStore storedkeyStore = keyStoreService.getKeyStoreUsingEncryptedDEK(dataModel.getEncryptedDEK());
			if (storedkeyStore == null) {

				return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("NO Such DEK. Entered DEK is Invalid");
			}
			String storedHash = storedkeyStore.getHashkey();
			String currentHash = Hashing.calculateSHA256Hash(dataModel.getEncryptedDEK());
			if (!storedHash.equals(currentHash)) {
				return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("NO Such DEK. Entered DEK is Invalid");
			}
			// fetch from aws
			String encryptedAdmin1 = secretsManagerService.fetchSecretValue("KMSAppSecrets", "Admin1");
			String encryptedAdmin2 = secretsManagerService.fetchSecretValue("KMSAppSecrets2", "Admin2");

			// decrpt admin1 and admin2
			String admin1 = kmsService.decrypt(encryptedAdmin1);
			String admin2 = kmsService.decrypt(encryptedAdmin2);

			String combinedKEK = dataEncryptionService.combineKeys(admin1, admin2);
			String decryptedDEK = kmsService.decryptWithCustomKey(dataModel.getEncryptedDEK(), combinedKEK);

			// Encrypt Sensitive data using DEK
			String encryptedData = kmsService.encryptWithCustomKey(dataModel.getData(), decryptedDEK);
			return ResponseEntity.ok(SensitiveDataEncryptionResponse.builder().encryptedData(encryptedData)
					.status("Sensitive Data Encrypted Successfully.").build());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong.");
	}

	@PreAuthorize("hasRole('ROLE_SUPERADMIN')")
	@PostMapping("/decrypt")
	public ResponseEntity<?> decryptSensitiveData(
			@RequestBody SensitiveDataDecryptionRequest sensitiveDataDecryptionRequest) {
		// verify dek
		KeyStore storedkeyStore = keyStoreService
				.getKeyStoreUsingEncryptedDEK(sensitiveDataDecryptionRequest.getEncryptedDEK());
		if (storedkeyStore == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("NO Such DEK. Entered DEK is Invalid");
		}
		String storedHash = storedkeyStore.getHashkey();
		String currentHash = Hashing.calculateSHA256Hash(sensitiveDataDecryptionRequest.getEncryptedDEK());
		if (!storedHash.equals(currentHash)) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("NO Such DEK. Entered DEK is Invalid");
		}
		String encryptedAdmin1 = secretsManagerService.fetchSecretValue("KMSAppSecrets", "Admin1");
		String encryptedAdmin2 = secretsManagerService.fetchSecretValue("KMSAppSecrets2", "Admin2");
		// decrpt admin1 and admin2
		String admin1 = kmsService.decrypt(encryptedAdmin1);
		String admin2 = kmsService.decrypt(encryptedAdmin2);
		String combinedKEK = dataEncryptionService.combineKeys(admin1, admin2);

		try {
			String decryptedDEK = kmsService.decryptWithCustomKey(sensitiveDataDecryptionRequest.getEncryptedDEK(),
					combinedKEK);
			String decryptedData = kmsService.decryptWithCustomKey(sensitiveDataDecryptionRequest.getEncryptedData(),
					decryptedDEK);

			return ResponseEntity.ok(SensitiveDataDecryptionResponse.builder().decryptedData(decryptedData)
					.status("Sensitive Data Decrypted Successfully.").build());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong.");
	}

}
