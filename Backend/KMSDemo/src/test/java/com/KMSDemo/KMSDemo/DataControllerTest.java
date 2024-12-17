package com.KMSDemo.KMSDemo;

import com.KMSDemo.KMSDemo.Config.SecretsManagerService;
import com.KMSDemo.KMSDemo.Controllers.DataController;
import com.KMSDemo.KMSDemo.Entities.KeyStore;
import com.KMSDemo.KMSDemo.Models.SensitiveDataDecryptionRequest;
import com.KMSDemo.KMSDemo.Models.SensitiveDataDecryptionResponse;
import com.KMSDemo.KMSDemo.Models.SensitiveDataEncryptionRequest;
import com.KMSDemo.KMSDemo.Models.SensitiveDataEncryptionResponse;
import com.KMSDemo.KMSDemo.Services.DataEncryptionService;
import com.KMSDemo.KMSDemo.Services.KeyStoreService;
import com.KMSDemo.KMSDemo.Services.KmsService;
import com.KMSDemo.KMSDemo.Util.Hashing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DataControllerTest {

    @Mock
    private KmsService kmsService;

    @Mock
    private KeyStoreService keyStoreService;

    @Mock
    private DataEncryptionService dataEncryptionService;

    @Mock
    private SecretsManagerService secretsManagerService;

    @InjectMocks
    private DataController dataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes all mocks
    }

    @Test
    void testEncryptSensitiveData_Success() throws Exception {
        // Input preparation
        SensitiveDataEncryptionRequest request = new SensitiveDataEncryptionRequest();
        request.setData("SensitiveData");
        request.setEncryptedDEK("EncryptedDEK123");

        // Mocking dependencies
        KeyStore keyStore = new KeyStore();
        keyStore.setHashkey(Hashing.calculateSHA256Hash("EncryptedDEK123"));

        when(keyStoreService.getKeyStoreUsingEncryptedDEK("EncryptedDEK123")).thenReturn(keyStore);
        when(secretsManagerService.fetchSecretValue("KMSAppSecrets", "Admin1")).thenReturn("Admin1Encrypted");
        when(secretsManagerService.fetchSecretValue("KMSAppSecrets2", "Admin2")).thenReturn("Admin2Encrypted");

        when(kmsService.decrypt("Admin1Encrypted")).thenReturn("Admin1");
        when(kmsService.decrypt("Admin2Encrypted")).thenReturn("Admin2");
        when(dataEncryptionService.combineKeys("Admin1", "Admin2")).thenReturn("CombinedKEK");

        when(kmsService.decryptWithCustomKey("EncryptedDEK123", "CombinedKEK")).thenReturn("DecryptedDEK");
        when(kmsService.encryptWithCustomKey("SensitiveData", "DecryptedDEK")).thenReturn("EncryptedData");

        // Execute controller method
        ResponseEntity<?> response = dataController.encryptSensitiveData(request);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SensitiveDataEncryptionResponse responseBody = (SensitiveDataEncryptionResponse) response.getBody();
        assertEquals("EncryptedData", responseBody.getEncryptedData());
        assertEquals("Sensitive Data Encrypted Successfully.", responseBody.getStatus());

        // Verifications
        verify(keyStoreService, times(1)).getKeyStoreUsingEncryptedDEK("EncryptedDEK123");
        verify(kmsService, times(1)).decryptWithCustomKey("EncryptedDEK123", "CombinedKEK");
        verify(kmsService, times(1)).encryptWithCustomKey("SensitiveData", "DecryptedDEK");
    }

    @Test
    void testEncryptSensitiveData_InvalidDEK() {
        // Input preparation
        SensitiveDataEncryptionRequest request = new SensitiveDataEncryptionRequest();
        request.setEncryptedDEK("InvalidDEK");

        // Mock response for invalid DEK
        when(keyStoreService.getKeyStoreUsingEncryptedDEK("InvalidDEK")).thenReturn(null);

        // Execute controller method
        ResponseEntity<?> response = dataController.encryptSensitiveData(request);

        // Assertions
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals("NO Such DEK. Entered DEK is Invalid", response.getBody());

        // Verifications
        verify(keyStoreService, times(1)).getKeyStoreUsingEncryptedDEK("InvalidDEK");
        verifyNoInteractions(kmsService, secretsManagerService, dataEncryptionService);
    }

    @Test
    void testDecryptSensitiveData_Success() throws Exception {
        // Input preparation
        SensitiveDataDecryptionRequest request = new SensitiveDataDecryptionRequest();
        request.setEncryptedDEK("EncryptedDEK123");
        request.setEncryptedData("EncryptedSensitiveData");

        // Mocking dependencies
        KeyStore keyStore = new KeyStore();
        keyStore.setHashkey(Hashing.calculateSHA256Hash("EncryptedDEK123"));

        when(keyStoreService.getKeyStoreUsingEncryptedDEK("EncryptedDEK123")).thenReturn(keyStore);
        when(secretsManagerService.fetchSecretValue("KMSAppSecrets", "Admin1")).thenReturn("Admin1Encrypted");
        when(secretsManagerService.fetchSecretValue("KMSAppSecrets2", "Admin2")).thenReturn("Admin2Encrypted");

        when(kmsService.decrypt("Admin1Encrypted")).thenReturn("Admin1");
        when(kmsService.decrypt("Admin2Encrypted")).thenReturn("Admin2");
        when(dataEncryptionService.combineKeys("Admin1", "Admin2")).thenReturn("CombinedKEK");

        when(kmsService.decryptWithCustomKey("EncryptedDEK123", "CombinedKEK")).thenReturn("DecryptedDEK");
        when(kmsService.decryptWithCustomKey("EncryptedSensitiveData", "DecryptedDEK"))
                .thenReturn("DecryptedSensitiveData");

        // Execute controller method
        ResponseEntity<?> response = dataController.decryptSensitiveData(request);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SensitiveDataDecryptionResponse responseBody = (SensitiveDataDecryptionResponse) response.getBody();
        assertEquals("DecryptedSensitiveData", responseBody.getDecryptedData());
        assertEquals("Sensitive Data Decrypted Successfully.", responseBody.getStatus());

        // Verifications
        verify(keyStoreService, times(1)).getKeyStoreUsingEncryptedDEK("EncryptedDEK123");
        verify(kmsService, times(1)).decryptWithCustomKey("EncryptedSensitiveData", "DecryptedDEK");
    }
}
