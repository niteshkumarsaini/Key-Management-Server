package com.KMSDemo.KMSDemo.Models;

public class DEKEncryptionRequest {

    private String admin1EncryptedKey;
    private String admin2EncryptedKey;
    private String encryptedDek;
    private String plainText;
    private String encryptedData;
	public String getAdmin1EncryptedKey() {
		return admin1EncryptedKey;
	}
	public void setAdmin1EncryptedKey(String admin1EncryptedKey) {
		this.admin1EncryptedKey = admin1EncryptedKey;
	}
	public String getAdmin2EncryptedKey() {
		return admin2EncryptedKey;
	}
	public void setAdmin2EncryptedKey(String admin2EncryptedKey) {
		this.admin2EncryptedKey = admin2EncryptedKey;
	}
	public String getEncryptedDek() {
		return encryptedDek;
	}
	public void setEncryptedDek(String encryptedDek) {
		this.encryptedDek = encryptedDek;
	}
	public String getPlainText() {
		return plainText;
	}
	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}
	public String getEncryptedData() {
		return encryptedData;
	}
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

    // Getters and setters
    
    
    
    
}
