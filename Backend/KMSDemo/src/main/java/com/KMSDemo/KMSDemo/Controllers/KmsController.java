package com.KMSDemo.KMSDemo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.KMSDemo.KMSDemo.Config.SecretsManagerService;
import com.KMSDemo.KMSDemo.Entities.KeyStore;
import com.KMSDemo.KMSDemo.Models.Admin1Response;
import com.KMSDemo.KMSDemo.Models.Admin2Response;
import com.KMSDemo.KMSDemo.Models.DEKEncryptionRequest;
import com.KMSDemo.KMSDemo.Models.DEKEncryptionResponse;
import com.KMSDemo.KMSDemo.Services.AuditService;
import com.KMSDemo.KMSDemo.Services.DataEncryptionService;
import com.KMSDemo.KMSDemo.Services.KeyStoreService;
import com.KMSDemo.KMSDemo.Services.KmsService;
import com.KMSDemo.KMSDemo.Util.Hashing;
import com.KMSDemo.KMSDemo.Util.RandomKeyGenerator;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/keys")
public class KmsController {

    private final KmsService kmsService;
    private final DataEncryptionService dataEncryptionService;
    
    @Autowired
    private AuditService auditService;
   
    @Autowired
    private KeyStoreService keyStoreService;
    
    private final SecretsManagerService secretsManagerService;

    public KmsController(KmsService kmsService, DataEncryptionService dataEncryptionService,SecretsManagerService secretsManagerService) {
        this.kmsService = kmsService;
        this.dataEncryptionService = dataEncryptionService;
   	 this.secretsManagerService=secretsManagerService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    @GetMapping("/admin1")
    public ResponseEntity<Admin1Response> generateAdmin1Key() {
        // Generate a random AES-128 key (16 bytes)
        String randomKey = RandomKeyGenerator.generateRandomKey(16); // 16 bytes = 128 bits
        String encryptedAdmin1=kmsService.encrypt(randomKey, "b5eda1c7-b238-4eb2-92c3-322ffc002c77");
        secretsManagerService.storeOrUpdateIndividualKey("KMSAppSecrets","Admin1" , encryptedAdmin1);
        return ResponseEntity.ok(Admin1Response.builder().encryptedAdmin1Key(encryptedAdmin1).Status("Admin1 key Generated and Encrypted Successfully.").build());
        
    }

    @PreAuthorize("hasRole('ROLE_ADMIN2')")
    @GetMapping("/admin2")
    public ResponseEntity<Admin2Response> generateAdmin2Key() {
        // Generate a random AES-128 key (16 bytes)
        String randomKey = RandomKeyGenerator.generateRandomKey(16); // 16 bytes = 128 bits
        String encryptedAdmin2=kmsService.encrypt(randomKey, "b5eda1c7-b238-4eb2-92c3-322ffc002c77");
        secretsManagerService.storeOrUpdateIndividualKey("KMSAppSecrets2","Admin2" , encryptedAdmin2);
        return ResponseEntity.ok(Admin2Response.builder().encryptedAdmin2Key(encryptedAdmin2).Status("Admin2 key Generated and Encrypted Successfully.").build());
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PostMapping("/generateDEK")
    public ResponseEntity<DEKEncryptionResponse> generateDEK(@RequestBody DEKEncryptionRequest request,HttpServletRequest requests) {
        // Decrypt the Admin keys
        String admin1Key = kmsService.decrypt(request.getAdmin1EncryptedKey());
        String admin2Key = kmsService.decrypt(request.getAdmin2EncryptedKey());
        // Combine the two keys
        String combinedKey = dataEncryptionService.combineKeys(admin1Key, admin2Key);
 
        // Generate a random DEK (16 bytes = 128 bits)
        String dek = RandomKeyGenerator.generateRandomKey(16);  // AES-128 DEK

        try { 
            // Encrypt the DEK using the combined key (KEK)
            String encryptedDek = kmsService.encryptWithCustomKey(dek, combinedKey);
           //Hashing the dek
            String DEKHashKey=Hashing.calculateSHA256Hash(encryptedDek);
//            //saving to db
            KeyStore keyStore=KeyStore.builder().encryptedDEK(encryptedDek).hashkey(DEKHashKey).build();
            keyStoreService.save(keyStore);
            //setting audit logs
            auditService.logEvent("DEK generated", requests, "SUCCESS", "DEK Hash: " + DEKHashKey);
  
            return ResponseEntity.ok(DEKEncryptionResponse.builder().encryptedDEK(encryptedDek).status("DEK is Generated and Encrypted Successfully.").build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
