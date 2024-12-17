package com.KMSDemo.KMSDemo.Services;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.InvalidKeyException;
import java.util.Base64;

@Service
public class KmsService {

    private final AWSKMS awsKmsClient;

    public KmsService(AWSKMS awsKmsClient) {
        this.awsKmsClient = awsKmsClient;
    }

    /**
     * Encrypts the plaintext using AWS KMS with the provided KMS Key ID.
     * 
     * @param plainText the plaintext to encrypt.
     * @param keyId the AWS KMS Key ID.
     * @return the encrypted data in Base64 encoding.
     */
    public String encrypt(String plainText, String keyId) {
        EncryptRequest request = new EncryptRequest()
                .withKeyId(keyId)
                .withPlaintext(ByteBuffer.wrap(plainText.getBytes()));
        ByteBuffer encryptedBytes = awsKmsClient.encrypt(request).getCiphertextBlob();
        return Base64.getEncoder().encodeToString(encryptedBytes.array());
    }

    /**
     * Decrypts the given encrypted data using AWS KMS.
     * 
     * @param encryptedText the encrypted data in Base64 encoding.
     * @return the decrypted plaintext.
     */
    public String decrypt(String encryptedText) {
        ByteBuffer encryptedBytes = ByteBuffer.wrap(Base64.getDecoder().decode(encryptedText));
        DecryptRequest request = new DecryptRequest().withCiphertextBlob(encryptedBytes);
        ByteBuffer decryptedBytes = awsKmsClient.decrypt(request).getPlaintext();
        return new String(decryptedBytes.array());
    }

    /**
     * Encrypts data using AES with a custom key (e.g., combinedAdminKey).
     * 
     * @param data the data to encrypt (e.g., DEK).
     * @param key the custom encryption key to use (e.g., combinedAdminKey).
     * @return the encrypted data in Base64 encoding.
     * @throws InvalidKeyException if the key length is invalid.
     */
  
    public String encryptWithCustomKey(String data, String key) throws Exception {
        // Ensure the key length is valid for AES encryption (16, 24, or 32 bytes)
        String combinedKey = key;  // Assuming 'key' is the combined key
        
        // Hash the combined key (e.g., using SHA-256) to get a 32-byte AES key
        byte[] hashedKey = hashKey(combinedKey);
        
        // Create AES key from the hashed key (it will now be 32 bytes for AES-256)
        Key aesKey = new SecretKeySpec(hashedKey, "AES");
        
        // Initialize the cipher for encryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        
        // Encrypt the data
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        
        // Return the encrypted data in Base64 encoding
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decryptWithCustomKey(String encryptedData, String key) throws Exception {
        // Ensure the key length is valid for AES encryption (16, 24, or 32 bytes)
        String combinedKey = key;  // Assuming 'key' is the combined key
        
        // Hash the combined key (e.g., using SHA-256) to get a 32-byte AES key
        byte[] hashedKey = hashKey(combinedKey);
        
        // Create AES key from the hashed key (it will now be 32 bytes for AES-256)
        Key aesKey = new SecretKeySpec(hashedKey, "AES");
        
        // Initialize the cipher for decryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        
        // Decrypt the data
        byte[] decodedEncryptedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedEncryptedData);
        
        // Return the decrypted data as string
        return new String(decryptedBytes);
    }

    private byte[] hashKey(String key) throws NoSuchAlgorithmException {
        // Use SHA-256 to hash the combined key (produces a 32-byte key for AES-256)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(key.getBytes());
    }
}
