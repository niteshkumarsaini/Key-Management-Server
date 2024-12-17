package com.KMSDemo.KMSDemo.Config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.*;
import software.amazon.awssdk.services.secretsmanager.model.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecretsManagerService {
	
    @Value("${aws.config.secretmanagerAccessKey}")
    private String awsAccessKey;  // Set your AWS access key here
    
    @Value("${aws.config.secretmanagerSecretKey}")
    private String awsSecretKey;  // Set your AWS secret key here
    
    @Value("${aws.config.secretmanagerRegion}")
    private String awsRegion;  // Specify your AWS region
	

	private final SecretsManagerClient secretsManager;

	public SecretsManagerService() {
		// Initialize the Secrets Manager client with your AWS credentials (access key
		// and secret key)
		AwsBasicCredentials credentials = AwsBasicCredentials.create("",
				"");
		this.secretsManager = SecretsManagerClient.builder()
				.credentialsProvider(StaticCredentialsProvider.create(credentials)).region(Region.of("ap-south-1")) 
				.build();
	}

	// Method to store or update the secret in Secrets Manager
	public void storeOrUpdateSecret(String secretName, String admin1Key, String admin2Key) {
		try {

			// Prepare the secret values to be stored
			Map<String, String> secretValues = new HashMap<>();
			secretValues.put("admin1Key", admin1Key);
			secretValues.put("admin2Key", admin2Key);
			// Convert the secret values to a JSON-like string
			String secretValueJson = secretValues.toString();

			// Check if the secret exists
			try {
				// Try to get the secret to check if it exists
				GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName)
						.build();
				secretsManager.getSecretValue(getSecretValueRequest);

				// If it exists, update the secret
				UpdateSecretRequest updateSecretRequest = UpdateSecretRequest.builder().secretId(secretName)
						.secretString(secretValueJson).build();
				secretsManager.updateSecret(updateSecretRequest);

			} catch (ResourceNotFoundException e) {
				// If the secret does not exist, create a new one
				CreateSecretRequest createSecretRequest = CreateSecretRequest.builder().name(secretName)
						.secretString(secretValueJson).build();
				secretsManager.createSecret(createSecretRequest);
			}

		} catch (Exception e) {
			System.err.println("Error storing/updating secret: " + e.getMessage());
		}
	}

	// Method to store or update an individual key
	public void storeOrUpdateIndividualKey(String secretName, String keyName, String keyValue) {
		try {
			// Prepare the secret values with a single key-value pair
			Map<String, String> secretValues = new HashMap<>();
			secretValues.put(keyName, keyValue);

			// Convert the secret values to a JSON-like string
			String secretValueJson = secretValues.toString();

			// Check if the secret exists
			try {
				// Try to get the secret to check if it exists
				GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName)
						.build();
				secretsManager.getSecretValue(getSecretValueRequest);

				// If it exists, update the secret
				UpdateSecretRequest updateSecretRequest = UpdateSecretRequest.builder().secretId(secretName)
						.secretString(secretValueJson).build();
				secretsManager.updateSecret(updateSecretRequest);
			} catch (ResourceNotFoundException e) {
				// If the secret does not exist, create a new one
				CreateSecretRequest createSecretRequest = CreateSecretRequest.builder().name(secretName)
						.secretString(secretValueJson).build();
				secretsManager.createSecret(createSecretRequest);
			}

		} catch (Exception e) {
			System.err.println("Error storing/updating individual key: " + e.getMessage());
		}
	}

	// Method to fetch a secret from AWS Secrets Manager
	public Map<String, String> fetchSecret(String secretName) {
		try {
			GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).build();
			GetSecretValueResponse secretValueResponse = secretsManager.getSecretValue(getSecretValueRequest);

			// The secret is a string containing key-value pairs
			String secretString = secretValueResponse.secretString();

			// Convert the secret string back into a Map
			Map<String, String> secretValues = new HashMap<>();
			if (secretString != null) {
				String[] keyValuePairs = secretString.replace("{", "").replace("}", "").split(",");
				for (String pair : keyValuePairs) {
					String[] keyValue = pair.split("=");
					secretValues.put(keyValue[0].trim(), keyValue[1].trim());
				}
			}
			return secretValues;
		} catch (Exception e) {
			System.err.println("Error fetching secret: " + e.getMessage());
			return null;
		}
	}

	// Method to fetch a secret value using secretName and keyName
	public String fetchSecretValue(String secretName, String keyName) {
		try {
			GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName) // Use
																												// the
																												// passed
																												// secret
																												// name
					.build();
			GetSecretValueResponse secretValueResponse = secretsManager.getSecretValue(getSecretValueRequest);

			// The secret is a string containing key-value pairs
			String secretString = secretValueResponse.secretString();

			if (secretString != null) {
				// Convert the secret string back into a Map
				String[] keyValuePairs = secretString.replace("{", "").replace("}", "").split(",");
				for (String pair : keyValuePairs) {
					String[] keyValue = pair.split("=");
					if (keyValue[0].trim().equals(keyName)) {
						return keyValue[1].trim();
					}
				}
			}
			return "Key not found";
		} catch (Exception e) {
			System.err.println("Error fetching secret: " + e.getMessage());
			return null;
		}
	}
}
